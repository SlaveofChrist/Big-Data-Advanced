# Big Data Advanced
This project aim to implement the concepts of DMS NoSQL

## How to run our project
  ### Creating the database and loading the various collections.
    First of all, download mongodb onto your machine with the appropriate utilities.
    Create the database with mongosh under the name bankAgency.

    Load the validations_database_crew.js file present in scripts with the load command.
    Import the collections from the data_json folder with the mongoimport command.
  ### Commands
    * use bankAgency
    * load("C:/Big-Data-Advanced/scripts/validations_database.js")
    * mongoimport -d=bankAgency -c=accounts --file=C:\Big-Data-Advanced\data_json\accounts.json --jsonArray  
    * mongoimport -d=bankAgency -c=clients --file=C:\Big-Data-Advanced\data_json\clients.json --jsonArray  
    * mongoimport -d=bankAgency -c=agencies --file=C:\Big-Data-Advanced\data_json\agencies.json --jsonArray  
    * mongoimport -d=bankAgency -c=managers --file=C:\Big-Data-Advanced\data_json\managers.json --jsonArray  
    * mongoimport -d=bankAgency -c=transactions --file=C:\Big-Data-Advanced\data_json\transactions.json --jsonArray  


## Authors
  * Eliel WOTOBE 
  * Romeo David AMEDOMEY
  * Tymoteusz Igor Cyryl CIESIELSKI
  * Mohamed ELYSALEM
