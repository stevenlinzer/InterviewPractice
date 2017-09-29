# Interview Practice

This site is dedicated to helping those that are trying to land a job as a Java programmer and they are facing any number of coding interviews and/or pre-interview coding assignments that potential employers have you do even before you are asked for a real interview. One philosophy that I will attempt to follow is that "regular" programmers solve problems using good algorithms, but "good" programmers solve problems using both good data structures and good algorithms. So, where ever I can make use of a standard data structure in order to either simplify the solution or to reduce the computational time of the solution, I will try to do so.
The following sections will discuss the data structures and problems that computer programmers and data scientists try to solve.

## Data Structures
It is advicable that an person preparing for a Java coding interview be able to implement any standard Java data structure. The simplest structures include the ArrayList, HashSet, and HashMap. It is a good idea to actually write your own implementations of these structures and test them out when you are writing a solution to a problem. You may learn more about how the Java language thgis way. Even if you do not do that, it is important to know how they work because questions about java's implementations of their structures comes up in interview **all the time**.
In addition, there are data structures that are used in solving problems that are not part of standard language. The following sections discusses them.

### Union Find
This structure is used when there are a known number of descrete data points and the solution requires these data points be arranged into groups of related data points. As such, the UnionFind's constructor requires to know the number of data points in the solution. When the UnionFind is constructed, all data points are not associated to any other point, i.e. each point is associated to itself only. There are two basic methods that allow you to query and modify the structure. The first is the union(pointA, pointB) which modifies the structure to assoiates pointA to pointB. The second method queries two points to see if they are related. The find(pointA, pointB) method returns a boolean true if points pointA and pointB are associated. Since I used Robert Sedgewick's implementation of this structure in which all associated points are connected in trees, I/we have added a root(point) that returns the root node of the point given. Since I have used this structure in the solutions of a few problems, I discuss the usage of this structure in the sub-sections of the Problems section ahead.

### Priority Queue
The standard priority queue stores queued data in a sorted for where the greatest element is always at the "top". This is done by implementing a heap. A heap is a tree where the root node is always the greatest node. When instanciated, the queue is empty. There are two public methods that interact with the Priority Queue's data. The insert(Key) adds a new key to the queue and deleteMax() deletes maximum (root) from the heap and returns it. There are two private methods, sink and swim, that adjust the heap whenever it is modified. (Robert Sedgewick).

## Problems
To Be Written

## Conclusion

I ask any viewers of this website that think that they have a better solution to any problem shown here, to please contact me.
