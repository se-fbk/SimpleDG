default: dotChecker

program.o: dotChecker.c
	gcc -c dotChecker.c -o dotChecker.o

program: dotChecker.o
	gcc dotChecker.o -o dotChecker

clean:
	-rm -f dotChecker.o
	-rm -f dotChecker
