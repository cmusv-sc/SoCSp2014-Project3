Implementation of search of DBLP using mongoDB.  To use, compile and run:

> java DBLPParser

If using bash, the following will allow you to set up your environment before compiling:

> source setup.sh

---------------------------------------------------------------------

Example: Create database

> Do you want to create the database? [y/n] y

---------------------------------------------------------------------

Example: Search for author

> Enter an author ('x' to exit): Massimo Zancanaro
{ "_id" : { "$oid" : "5317bca90cf2e5208112a58f"} , "type" : "incollection" , "title" : "Shared Interfaces for Co-located Interaction." , "pages" : "71-88" , "year" : "2012" , "booktitle" : "Ubiquitous Display Environments"  , "ee" : "http://dx.doi.org/10.1007/978-3-642-27663-7_5" , "crossref" : "series/cogtech/364227662" , "url" : "db/series/cogtech/364227662.html#Zancanaro12" , "author" : [ "Massimo Zancanaro"]}
{ "_id" : { "$oid" : "5317bca90cf2e5208112a5d7"} , "type" : "incollection" , "title" : "Analysis and Prediction of Museum Visitors' Behavioral Pattern Types." , "pages" : "161-176" , "year" : "2012" , "booktitle" : "Ubiquitous Display Environments" , "ee" : "http://dx.doi.org/10.1007/978-3-642-27663-7_10" , "crossref" : "series/cogtech/364227662" , "url" : "db/series/cogtech/364227662.html#KuflikBZ12" , "author" : [ "Tsvi Kuflik" , "Zvi Boger" , "Massimo Zancanaro"]}

---------------------------------------------------------------------

Example: Search for multiple authors

Enter an author ('x' to exit): Andre Berton', 'Alfred Kaltenmeier
{ "_id" : { "$oid" : "5317bca90cf2e5208112a591"} , "type" : "incollection" , "title" : "Speech Recognition." , "pages" : "85-107" , "year" : "2006" , "booktitle" : "SmartKom" , "ee" : "http://dx.doi.org/10.1007/3-540-36678-4_6" , "crossref" : "series/cogtech/54023732" , "url" : "db/series/cogtech/54023732.html#BertonKHS06" , "author" : [ "Andre Berton" , "Alfred Kaltenmeier" , "Udo Haiber" , "Olaf Schreiner"]}
{ "_id" : { "$oid" : "5317bca90cf2e5208112a5a1"} , "type" : "incollection" , "title" : "SmartKom-Mobile Car: User Interaction with Mobile Services in a Car Environment." , "pages" : "523-537" , "year" : "2006" , "booktitle" : "SmartKom" , "ee" : "http://dx.doi.org/10.1007/3-540-36678-4_33" , "crossref" : "series/cogtech/54023732" , "url" : "db/series/cogtech/54023732.html#BertonBM06" , "author" : [ "Andre Berton" , "Dirk Buhler" , "Wolfgang Minker"]}


---------------------------------------------------------------------

Enter an author ('x' to exit): x
