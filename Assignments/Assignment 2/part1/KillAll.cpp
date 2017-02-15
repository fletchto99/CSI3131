#include <iostream>
#include <cstring>
#include <cstdlib>
#include <cctype>

#include <unistd.h>
#include <signal.h>
#include <string.h>

using namespace std;

int main(int argc, const char *argv[]) {
    if (argc < 2) {
        cout << "Usage: KillAll <process>" << endl;
        return 1;
    }
    
    const char *toKill = argv[1];

    int parent_pipe[2];
    int child_pipe[2];
    
    cout << "[Parent] Creating parent pipe" << endl;
    if (pipe(parent_pipe) == -1) {
        cerr << "[Parent] Failed to create parent pipe" << endl;
        return 1;
    }
    
    cout << "[Parent] Creating child pipe" << endl;
    if (pipe(child_pipe) == -1) {
        cerr << "[Parent] Failed to create child pipe" << endl;
        return 1;
    }
    
    cout << "[Parent] Forking process" << endl;
    pid_t pid = fork();
    if (pid == -1) {
        cerr << "[Parent] Failed to fork process" << endl;
        return 1;
    }
    
    if (pid == 0) {
        cout << "[Child] Piping to parent process" << endl;
        char buf[8];

        dup2(parent_pipe[1], 1);

        if (read(child_pipe[0], buf, sizeof(buf)) != -1) {
            if (strcmp(buf, "LISTALL") == 0) {
                execlp("ps", "ps", "-eww", "-o", "pid=", "-o", "comm=", NULL);
            }
        }
    } else {
        cout << "[Parent] Sending message to child pipe" << endl;
        write(child_pipe[1], "LISTALL", 8);
        
        // Read response line-by-line from child
        cout << "[Parent] Receiving process list from child" << endl;

        string line;
        char buf[1024];
        ssize_t len;
        size_t numKills = 0;
        while ((len = read(parent_pipe[0], buf, sizeof(buf))) != -1) {
            // Accumulate line untill newline is found
            size_t start = 0;
            for (size_t i = 0; i < len; i++) {
                if (buf[i] == '\n') {
                    line.append(buf + start, buf + i);
                    if (strstr(line.c_str(), toKill) != NULL) {
                        numKills += kill(strtol(line.c_str(), NULL, 10), SIGKILL) == 0;
                    }
                    line.clear();
                    start = i + 1;
                }
            }
            // Accumulate left-overs if newline wasn't found
            line.append(buf + start, buf + len);

            // Handle end of response
            if (len != sizeof(buf)) {
                if (strstr(line.c_str(), toKill) != NULL) {
                    numKills += kill(strtol(line.c_str(), NULL, 10), SIGKILL) == 0;
                }
                break;
            }
        }
        cout << "[Parent] Killed " << numKills << " processes with the name " << toKill << endl;
    }
    
    return 0;
}
