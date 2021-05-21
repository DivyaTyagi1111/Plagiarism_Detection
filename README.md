# Plagiarism Detection using Java Relationship Graph (JRG)

## The objectives of this project are listed below: -
1. Detecting plagiarism using elements-based similarity.
2. Combining the results of call graph approach with counting based approach using JRG.
3. Devise the efficient and powerful similarity index to detect plagiarism.

## Steps to run the code
0. Clone the repository
1. Follow the steps given in the document "Steps for installation and running neo4j community embedded version" on how to set up and run neo4j using command prompt.
2. Import the project and let it build and resolve all maven dependencies.
3. Change the path of Java files as per your requirement in the main method of file "ExtendedJRGCreator.java" and run this file.
4. Check the output in console window of eclipse.
5. To check the recreation of java project from neo4j graphs 
   ExtendedJRGConvertor convertor = new ExtendedJRGConvertor(graphDb); will make a call to convertor code before shutdown 
   Comment it out if you don't want this functionality.
6. To check how much percentage overlap in recreation use an ubuntu system with the tool "dwdiff" and just run the command
   "dwdiff -l -s firstFileName secondFileName"
7. Make sure to delete all created files before running the project again. Like both the neo4jDb files and both the project's recreated from graph.

Tip:  To visualize graphs and run cql queries use the attached document "Steps for installation and running neo4j community embedded version" to see how to open neo4j in browser.

