#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <signal.h>
#include <unistd.h>
#include <stdlib.h>

/* the program execution starts here */
int main(int argc, char **argv){
    char    *program;

    if (argc != 2) {
        printf("Usage: mon fileName");
        exit(-1);
    } else {
        program = argv[1];

        pid_t pid;

        /* fork a child process */
        pid = fork(); 

        if (pid < 0) {
            /* error occurred */ 
            fprintf(stderr, "Fork Failed"); 
            return -1; 
        } else if (pid == 0) { 
            /* child process */
            execl(program, program, NULL);
        } else { 

            pid_t procmon;

            /* fork a child process */
            procmon = fork(); 

            if (procmon < 0) {
                /* error occurred */ 
                fprintf(stderr, "Procmon fork Failed"); 
                return -1; 
            } else if (procmon == 0) { 
                char buffer[20];
                sprintf(buffer, "%ld", (long)pid);
                /* child process */
                execl("procmon", "procmon", buffer, NULL);
            } else { 
                sleep(20);
                kill(pid, 15);
                sleep(2);
                kill(procmon, 15); 
            } 
        } 
        return 0; 
    }
}