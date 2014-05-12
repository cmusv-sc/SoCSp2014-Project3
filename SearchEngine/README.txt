There are three parts to this project

1.) python_src: Contains code to process ProgrammableWeb data from a MongoDB database.

2.) build.xml; src: Ant project to process ProgrammableWeb data from python_src and place it into the WSO2 Governance Registry.

3.) run_csv.sh: Script that uses a pre-made CSV file in python_src/PW.zip to deploy the service search engine as a web service.

To use, unzip PW.zip and execute

> run_csv.sh

This should set up your classpath and start the python webserver.
