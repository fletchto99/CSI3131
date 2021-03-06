# KillAll - Java RMI version

This simple utility will send a SIGTERM signal to a process
to kill all processes running with that name

*Notes:*
- Only supports unix-like machines (Tested on Kali)
- The response from LISTALL is not logged since there is plenty of output
- Running "KillAll" launches the server followed by the client
- The server will continue to run until the process is manually killed

### Compilation:
`javac KillAll.java`

### Usage:
`java KillAll <process>`

### Sample output:

````
root@kali:/mnt/hgfs/Kali/CSI3131# java KillAll firefox
[Server] Starting KillAllApp port 8080
[Client] Connecting to localhost:8080
[Client] Calling listAll() method
[Server] Complete
[Server] Client called listAll()
[Client] Attempted to kill 0 processes with the name firefox
[Client] Finished killing processes
````