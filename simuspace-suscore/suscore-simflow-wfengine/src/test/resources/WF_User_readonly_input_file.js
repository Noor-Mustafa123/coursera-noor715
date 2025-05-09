{
  "id": "dc59f392-6abe-11e6-8b77-86f30ca893d3", // uuid
  "name": "Huzaifah's workflow",
  "interactive": false, //does require user interaction,
  "owner": {
    "id": 1234,
    "name": "Huzaifah",
    "emailAddress": "huzaifah.mubbashir@soco-engineers.de"
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
      "key": "section",
      "type":"template"

    }, {
      "name": "file1", //can be changed by the user to give this parameter a name/key/cmd/variable
      "value": "/home/sces138/readOnly.txt",
      "key": "file",
      "type":"user"
    }]}, {
    "id": "dc59f392-6abe-11e6-4444-86f30ca893d4", // uuid
    "name": "Output_Huz", //user-filled
    "key": "wfe_io",
    "comments": "User filled output values",
    "version": "0.0.1",
    "fields":[{
      "name": "header", //can be changed by the user to give this parameter a name/key/cmd/variable
      "value": "User Ouput Parameters",
      "key": "section",
      "type":"template"
    },{
      "name": "file2", //can be changed by the user to give this parameter a name/key/cmd/variable
      "value": "/home/sces138/readOnly.txt",
      "key": "file",
      "type":"user"
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
      "key": "section",
      "type":"template"
    }, {
      "name": "syntax",
      "value": "bash",
      "key": "dropdown",
      "type":"template"
    }, {
      "name": "script",
      "value": "/bin/cp {Input_Huz.file1} {Output_Huz.file2} ",
      "key": "textarea",
      "type":"template"
    }],
    "runMode": "client" // can be [client,server, user-selected, null]


  }],

  "connections": [{
    "source": "dc59f392-6abe-11e6-4444-86f30ca893d3",
    "target": "dc59f392-6abe-11e6-4444-86f30ca893d5"
  }, {
    "source": "dc59f392-6abe-11e6-4444-86f30ca893d5",
    "target": "dc59f392-6abe-11e6-4444-86f30ca893d4"
  }],
  "positions": {
    "dc59f392-6abe-11e6-8b77-86f30ca893d3": "377:510",
    "dc59f392-6abe-11e6-4444-86f30ca893d5": "477:510",
    "dc59f392-6abe-11e6-4444-86f30ca893d4": "577:510"
  } // ui related positioning info

}
