{
  "id": "dc59f392-6abe-11e6-8b77-86f30ca893d3", // uuid
  "name": "Huzaifah's workflow",
  "interactive": false, //does require user interaction,
  "owner": {
    "id": "dc59f392-6abe-11e6-4444-86f30ca893d3",
    "firstName": "Huzaifah"
  },
  "elements": [{
    "id": "dc59f392-6abe-11e6-4444-86f30ca893d3", // uuid
    "name": "Input_Huz", //user-filled
    "key": "wfe_io",
    "comments": "User filled input values",
    "version": "0.0.1",
    "fields": [{
      "name": "header", //can be changed by the user to give this parameter a name/key/cmd/variable
      "value": "User Input Parameters",
      "type": "section",
      "mode":"template"

    }, {
      "name": "file1", //can be changed by the user to give this parameter a name/key/cmd/variable
      "value": {
      "agent":"str", 
      "docId":0,
     "location":0,
     "path" : "src/test/resources/test.txt",
     "type": "client"
      },
      "type": "file",
      "mode":"user"
    }]}, {
    "id": "dc59f392-6abe-11e6-4444-86f30ca893d4", // uuid
    "name": "Output_Huz", //user-filled
    "key": "wfe_io",
    "comments": "User filled output values",
    "version": "0.0.1",
    "fields":[{
      "name": "header", //can be changed by the user to give this parameter a name/key/cmd/variable
      "value": "User Ouput Parameters",
      "type": "section",
      "mode":"template"
    },{
      "name": "file2", //can be changed by the user to give this parameter a name/key/cmd/variable
      "value": {
      "agent":"str", 
      "docId":0,
     "location":0,
     "path" : "src/test/resources/copy.txt",
     "type": "client"
      },
      "type": "file",
      "mode":"user"
    }]
  }, {
    "id": "dc59f392-6abe-11e6-4444-86f30ca893d5", // uuid
    "name": "Script_Huz",
    "key": "wfe_script",
    "comments": "User filled script",
    "version": "0.0.1",
    "fields": [{
      "name": "header",
      "value": "User Script",
      "type": "section",
      "mode":"template"
    }, {
      "name": "syntax",
      "value": "bash",
      "type": "dropdown",
      "mode":"template"
    }, {
      "name": "script",
      "value": "/bin/cp {Input_Huz.file1} {Output_Huz.file2} ",
      "type": "textarea",
      "mode":"template"
    }],
    "runMode": "client" // can be [client,server, user-selected, null]


  }],
  "positions": {
    "dc59f392-6abe-11e6-8b77-86f30ca893d3": "377:510",
    "dc59f392-6abe-11e6-4444-86f30ca893d5": "477:510",
    "dc59f392-6abe-11e6-4444-86f30ca893d4": "577:510"
  } // ui related positioning info

}
