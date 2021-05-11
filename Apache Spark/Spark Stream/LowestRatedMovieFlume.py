import re

from pyspark import SparkContext
from pyspark.streaming import StreamingContext
from pyspark.streaming.flume import FlumeUtils

# Take each line of u.data and convert it to (movieID, (rating, 1.0))
# This way we can then add up all the ratings for each movie, and
# the total number of ratings for each movie (which lets us compute the average)
def parseInput(line):
    fields = line.split()
    return (int(fields[1]), (float(fields[2]), 1.0))

if __name__ == "__main__":
    sc = SparkContext(appName="StreamingFlumeLogAggregator")
    sc.setLogLevel("ERROR")
    ssc = StreamingContext(sc, 1)

    flumeStream = FlumeUtils.createStream(ssc, "localhost", 44444)

    # Load up the raw u.data file
    lines = flumeStream.map(lambda x: x[1])
    
    # Convert to (movieID, (rating, 1.0))
    movieRatings = lines.map(parseInput)
    
    # Reduce to (movieID, (sumOfRatings, totalRatings)) over a 1-minute window sliding every second
    ratingTotalsAndCount = movieRatings.reduceByKeyAndWindow(lambda movie1, movie2: (movie1[0] + movie2[0], movie1[1] + movie2[1]), lambda movie1, movie2: (movie1[0] - movie2[0], movie1[1] - movie2[1]), 60, 1)
    
    # Filter the results
    moreThan10 = ratingTotalsAndCount.transform(lambda rdd: rdd.filter(lambda x: x[1][1] > 9 ))
    
    # Map to (rating, averageRating)
    averageRatings = moreThan10.transform(lambda rdd: rdd.mapValues(lambda totalAndCount : (totalAndCount[0] / totalAndCount[1], totalAndCount[1])))

    # Sort by average rating
    sortedMovies = averageRatings.transform(lambda rdd: rdd.sortBy(lambda x: x[1]))

    sortedMovies.pprint()

    ssc.checkpoint("/user/maria_dev/spark_work/checkpoint")
    ssc.start()
    ssc.awaitTermination()
