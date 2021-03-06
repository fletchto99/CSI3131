# KillAll - Java Socket version

This simple utility will send a SIGTERM signal to a process
to kill all processes running with that name

*Notes:*
- Only supports unix-like machines (Tested on Kali)
- The response from LISTALL is not logged since there is plenty of output
- Running "KillAll" launches the server followed by the client

### Compilation:
`javac KillAll.java`

### Usage:
`java KillAll <process>`

### Sample output:

```
root@kali:/mnt/hgfs/Kali/CSI3131# java KillAll firefox
[Server] Starting on port 8080
[Client] Connecting to localhost on port 8080
[Client] Sending LISTALL message
[Server] Received LISTALL message
[Server] Client called listAll()
[Server] Finished
[Client] Killing pid 13358
[Client] Attempted to kill 1 processes with the name firefox
[Client] Complete
```