# LogExtractor
Live services generate logs at a very high rate; e.g., some services create over 100,000 log lines a second. Usually, these logs are loaded inside databases to enable fast querying, but the cost of keeping all the logs becomes too high. For this reason, only the recent logs are kept in databases, and logs for longer periods are kept in file archives. For this problem, we should assume we store our data in multiple files. We close a file and start a new file when the file size reaches a predefined size. Our file names are of the format LogFile-######.log (e.g., LogFile- 000008.log, or LogFile-000139.log).   

LogExtractor is a tool that could extract the log lines from a given time range and print it to console in time effective manner.  

All the time formats will be "ISO 8601" format. 
Log file format
1. The log file has one log per line.
2. Every log line will start with TimeStamp in "ISO 8601" format followed by a comma (',').
3. All the log lines will be separated by a single newline character '\n'. 
4. Example logline: 2020-01-31T20:12:38.1234Z, Some Field, Other Field, And so on, Till new line,...\n 

Optimisations(which enabled fast searching and printing of logs): 
- Using ReversedFileReader to check and compare the last line with “from” timestamp was very useful in skipping files which don’t contain the timestamps of given duration 
- When the file containing the “from” timestamp has been found, to find the line containing “from” timestamp in this file and avoiding checking on every line, we can use the difference(Duration) in the first timestamp of the file and “from” timestamp to defer the checking until these many lines have been skipped. This makes the initial search very fast. 
- Assuming around 100000 logs are generated every second, which means that 100000 lines can be skipped for every second difference
- This same idea can be used, once the printing has started to avoid comparing every timstamp with the “to” timestamp 
- This parameter(number of lines to be skipped) is a safe estimate and once we reach the estimated line to be checked, if we haven’t reached the line containing “to” timestamp, we can again calculate the difference between, the current timestamp(the line we’re) currently reading and the “to” timestamp.
