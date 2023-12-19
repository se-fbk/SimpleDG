Task A:
a. Clone the "dot" branch from the github project https://github.com/se-fbk/SimpleDG

Follow the instruction to clone the "dot" branch on the local machine,

	Open Eclipse > File > Import > Git > Project from Git(With smart import) > Clone URL > add clone url from git (https://github.com/se-fbk/SimpleDG.git) > Select the branch(dot) > Check the packages > Finish




Task B:

To see the report for .dot file, Run the script from the command line by providing the directory containing the .dot files, for Example:

	python process_dot_files.py "path/to/directory"

this approach is for specific .dot file's report

To run the script for all .dot files in any specific directory run the script from the command line by providing the directory containing the .dot files. For Example:

	python graph_analyzer.py "path/to/directory"

Task C:

Follow same as mentioned in the Task B. By running those script it will also generate a folder named "output". Inside the "outout" folder all the report CSV will be stored and the name of the CSV will be same as the .dot file that it's generating the report. 
