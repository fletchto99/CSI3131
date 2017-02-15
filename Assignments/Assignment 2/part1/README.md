# KillAll C++ version

This simple utility will send a SIGTERM signal to a process
to kill all processes running with that name

*Notes:*
- A unix-like machine is required to run this program (Tested on Linux)
- The response from LISTALL is not logged since it added too much clutter.
- The steps processed by the child are not logged since stdout is redirected to the pipe.

### Compilation:
`g++ -o killall killall.cpp`

### Usage:
`./killall <process>`

### Sample output:

````
[Parent] Creating parent pipe
[Parent] Creating child pipe
[Parent] Forking process
[Parent] Sending message to child pipe
[Parent] Receiving process list from child
[Child] Piping to parent process
[Parent] Killed 2 processes with the name firefox
````