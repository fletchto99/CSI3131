/*--------------------------------------------------------------------------
File: mon2.c

Description: This program creates a process to run the program identified
             on the commande line.  It will then start procmon in another
	     process to monitor the change in the state of the first process.
	     After 20 seconds, signals are sent to the processes to terminate
	     them.

	     Also a third process is created to run the program filter.  
	     A pipe is created between the procmon process and the filter
	     process so that the standard output from procmon is sent to
	     the standard input of the filter process.
--------------------------------------------------------------------------*/
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <signal.h>

#define INPUT_END 1
#define OUTPUT_END 0
#define STDIN_FILENO


/* It's all done in main */
int main(int argc, char **argv) {
    char    *program;

    if (argc != 2) {
        printf("Usage: mon2 fileName\n where fileName is an executable file.\n");
        exit(-1);
    } else {
        program = argv[1];  /* This is the program to run and monitor */
    
        pid_t pid;

        /* fork a child process */
        pid = fork(); 

        if (pid < 0) {
            /* error occurred */ 
            fprintf(stderr, "Fork Failed"); 
            return -1; 
        } else if (pid == 0) { 
            /* First Step: Create the first process to run the program from the command line */
            execl(program, program, NULL);
        } else { 

            int fd[2];
            pid_t filter;


            /* Second step: Create the pipe to be used for connecting procmon to filter */
            pipe(fd);

            /* fork a child process */
            filter = fork(); 

            if (filter < 0) {
                /* error occurred */ 
                fprintf(stderr, "Fork Failed"); 
                return -1; 
            } else if (filter == 0) { 

                close(fd[0]);
                dup2(fd[1], 1);
                close(fd[1]);


                /* Third step: Lets create the filter process - don't forget to connect to the pipe */
                execl("filter", "filter", NULL);
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

                    close(fd[1]);
                    dup2(fd[0], 0);
                    close(fd[0]);
                        
                    /* Fourth step: Lets create the procmon process - don't forget to connect to the pipe */
                    execl("procmon", "procmon", buffer, NULL);
                } else { 
                    /* Fifth step: Let things run for 20 seconds */
                    sleep(20);

                    /* Last step: 
                       1. Kill the process running the program
                       2. Sleep for 2 seconds 
                       3. Kill the procmon and filter processes
                    */

                    kill(pid, 15);
                    sleep(2);
                    kill(procmon, 15); 
                    kill(filter, 15);
                } 
            }
        } 
    }
    return 0;
}



