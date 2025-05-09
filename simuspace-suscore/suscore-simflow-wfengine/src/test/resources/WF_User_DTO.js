{
	  "id": "dc59f392-6abe-11e6-8b77-86f30ca893d3",
	  "name": "Huzaifah's workflow",
	  "description": "",
	  "numberOfVersions": 0,
	  "numOfapprovedVersion": 0,
	  "createdOn": 1521194070680,
	  "createdBy": {
	    "id": "1e98a77c-a0be-4983-9137-d9a8acd0ea8b",
	    "uid": null,
	    "status": null,
	    "restricted": null,
	    "password": null,
	    "firstName": null,
	    "surName": null,
	    "groups": null,
	    "userDetails": null,
	    "susUserDirectoryDTO": null,
	    "userName": "null null",
	    "createdBy": null,
	    "modifiedBy": null,
	    "createdOn": null,
	    "modifiedOn": null,
	    "changable": false,
	    "profilePhoto": null,
	    "securityIdentity": null,
	    "editable": false,
	    "type": 0
	  },
	  "modifiedOn": 1521198406779,
	  "modifiedBy": {
	    "id": "1e98a77c-a0be-4983-9137-d9a8acd0ea8b",
	    "uid": null,
	    "status": null,
	    "restricted": null,
	    "password": null,
	    "firstName": null,
	    "surName": null,
	    "groups": null,
	    "userDetails": null,
	    "susUserDirectoryDTO": null,
	    "userName": "null null",
	    "createdBy": null,
	    "modifiedBy": null,
	    "createdOn": null,
	    "modifiedOn": null,
	    "changable": false,
	    "profilePhoto": null,
	    "securityIdentity": null,
	    "editable": false,
	    "type": 0
	  },
	  "comments": null,
	  "status": {
	    "id": 1,
	    "name": "WIP"
	  },
	  "token": null,
	  "version": {
	    "id": 4
	  },
	  "versions": null,
	  "interactive": false,
	  "jobs": {
	    "total": 15,
	    "completed": 3
	  },
	  "favorite": false,
	  "executable": true,
	  "curentSimUserId": null,
	  "elements": {
	    "nodes": [
	      {
	        "data": {
	          "id": "dc59f392-6abe-11e6-4444-86f30ca893d3",
	          "name": "Input_Huz",
	          "key": "wfe_io",
	          "comments": "User filled input values",
	          "info": {
	            "version": "0.0.1",
	            "description": "A workflow element which provides a list of possible user-filled inputs, and generates outputs which have an outgoing connection to <applicable> to be used as input at <applicable>",
	            "comments": "Specification developed by @Huzaifah"
	          },
	          "fields": [
	            {
	              "name": "header",
	              "value": "User Input Parameters",
	              "type": "section",
	              "mode": "template"
	            },
	            {
	              "name": "file1",
	              "value": {
	                "agent": "str",
	                "docId": 0,
	                "location": 0,
	                "path": "src/test/resources/test.txt",
	                "type": "client"
	              },
	              "type": "file",
	              "mode": "user"
	            }
	          ]
	        },
	        "position": {
	          "x": 188,
	          "y": 378
	        },
	        "group": "nodes",
	        "removed": false,
	        "selected": false,
	        "selectable": true,
	        "locked": false,
	        "grabbable": true,
	        "classes": "wfe_io"
	      },
	      {
	        "data": {
	          "id": "dc59f392-6abe-11e6-4444-86f30ca893d4",
	          "name": "Output_Huz",
	          "key": "wfe_io",
	          "comments": "User filled output values",
	          "info": {
	            "version": "0.0.1",
	            "description": "A workflow element which provides a list of possible user-filled inputs, and generates outputs which have an outgoing connection to <applicable> to be used as input at <applicable>",
	            "comments": "Specification developed by @Huzaifah"
	          },
	          "fields": [
	            {
	              "name": "header",
	              "value": "User Ouput Parameters",
	              "type": "section",
	              "mode": "template"
	            },
	            {
	              "name": "file2",
	              "value": {
	                "agent": "str",
	                "docId": 0,
	                "location": 0,
	                "path": "src/test/resources/copy.txt",
	                "type": "client"
	              },
	              "type": "file",
	              "mode": "user"
	            }
	          ]
	        },
	        "position": {
	          "x": 554,
	          "y": 377
	        },
	        "group": "nodes",
	        "removed": false,
	        "selected": true,
	        "selectable": true,
	        "locked": false,
	        "grabbable": true,
	        "classes": "wfe_script"
	      },
	      {
	        "data": {
	          "id": "dc59f392-6abe-11e6-4444-86f30ca893d5",
	          "name": "Script_Huz",
	          "key": "wfe_script",
	          "comments": "User filled script",
	          "info": {
	            "version": "0.0.1",
	            "description": "A workflow element which provides a list of possible user-filled inputs, and generates outputs which have an outgoing connection to <applicable> to be used as input at <applicable>",
	            "comments": "Specification developed by @Huzaifah"
	          },
	          "fields": [
	            {
	              "name": "header",
	              "value": "User Script",
	              "type": "section",
	              "mode": "template"
	            },
	            {
	              "name": "select",
	              "value": "bash",
	              "type": "dropdown",
	              "mode": "template"
	            },
	            {
	              "name": "script",
	              "value": "/bin/cp {{Input_Huz.file1}} {{Output_Huz.file2}} ",
	              "type": "textarea",
	              "mode": "template"
	            }
	          ],
	          "runMode": "client"
	        },
	        "position": {
	          "x": 554,
	          "y": 377
	        },
	        "group": "nodes",
	        "removed": false,
	        "selected": true,
	        "selectable": true,
	        "locked": false,
	        "grabbable": true,
	        "classes": "wfe_script"
	      },
	      {
	        "data": {
	          "id": "dc59f392-6abe-11e6-4444-86f30ca893d6",
	          "name": "SusExportFile",
	          "key": "wfe_export_files",
	          "comments": "User filled script",
	          "info": {
	            "version": "0.0.1",
	            "description": "A workflow element which provides a list of possible user-filled inputs, and generates outputs which have an outgoing connection to <applicable> to be used as input at <applicable>",
	            "comments": "Specification developed by @Huzaifah"
	          },
	          "fields": [
	            {
	              "name": "header",
	              "value": "User Script",
	              "type": "section",
	              "mode": "template"
	            },
	            {
	              "name": "syntax",
	              "value": "bash",
	              "type": "dropdown",
	              "mode": "template"
	            },
	            {
	              "name": "script",
	              "value": " {{ Input_Huz.object1 }} : {{ Output_Huz.file1 }} ",
	              "type": "textarea",
	              "mode": "template"
	            }
	          ],
	          "runMode": "client"
	        },
	        "position": {
	          "x": 554,
	          "y": 377
	        },
	        "group": "nodes",
	        "removed": false,
	        "selected": true,
	        "selectable": true,
	        "locked": false,
	        "grabbable": true,
	        "classes": "wfe_script"
	      }
	    ],
	    "edges": [
	      {
	        "data": {
	          "source": "dc59f392-6abe-11e6-4444-86f30ca893d3",
	          "target": "dc59f392-6abe-11e6-4444-86f30ca893d5",
	          "id": "a1ec5df9-bfda-4e75-8a51-6eccd4fc4435"
	        },
	        "position": {},
	        "group": "edges",
	        "removed": false,
	        "selected": false,
	        "selectable": true,
	        "locked": false,
	        "grabbable": true,
	        "classes": ""
	      },
	      {
	        "data": {
	          "source": "dc59f392-6abe-11e6-4444-86f30ca893d5",
	          "target": "dc59f392-6abe-11e6-4444-86f30ca893d4",
	          "id": "64d7e5f0-9195-45da-a705-ef58c3bc98ad"
	        },
	        "position": {},
	        "group": "edges",
	        "removed": false,
	        "selected": false,
	        "selectable": true,
	        "locked": false,
	        "grabbable": true,
	        "classes": ""
	      },
	      {
	        "data": {
	          "source": "dc59f392-6abe-11e6-4444-86f30ca893d3",
	          "target": "dc59f392-6abe-11e6-4444-86f30ca893d6",
	          "id": "97f95f61-f434-4569-83d1-46b85abdd035"
	        },
	        "position": {},
	        "group": "edges",
	        "removed": false,
	        "selected": false,
	        "selectable": true,
	        "locked": false,
	        "grabbable": true,
	        "classes": ""
	      },
	      {
	        "data": {
	          "source": "dc59f392-6abe-11e6-4444-86f30ca893d6",
	          "target": "dc59f392-6abe-11e6-4444-86f30ca893d4",
	          "id": "a1ec5df9-bfda-4e75-8a51-6eccd4fc4434"
	        },
	        "position": {},
	        "group": "edges",
	        "removed": false,
	        "selected": false,
	        "selectable": true,
	        "locked": false,
	        "grabbable": true,
	        "classes": ""
	      }
	    ]
	  },
	  "style": [
	    {
	      "selector": ".wfe_io",
	      "style": {
	        "shape": "octagon",
	        "background-color": "#32c5d2",
	        "background-image": "images/wf/sign-in.svg",
	        "label": "data(name)"
	      }
	    },
	    {
	      "selector": ".wfe_io.field-error",
	      "style": {
	        "background-color": "#CA5E58"
	      }
	    },
	    {
	      "selector": ".wfe_script",
	      "style": {
	        "shape": "rectangle",
	        "background-color": "#555",
	        "background-image": "images/wf/code.svg",
	        "label": "data(name)"
	      }
	    },
	    {
	      "selector": ".wfe_script.field-error",
	      "style": {
	        "background-color": "#CA5E58"
	      }
	    }
	  ],
	  "zoomingEnabled": true,
	  "userZoomingEnabled": true,
	  "zoom": 1.9955849889624724,
	  "minZoom": 0.1,
	  "maxZoom": 10,
	  "panningEnabled": true,
	  "userPanningEnabled": true,
	  "pan": {
	    "x": -233.37306843267106,
	    "y": -517.3024282560706
	  },
	  "boxSelectionEnabled": true,
	  "renderer": {
	    "name": "canvas"
	  },
	  "userSignature": null,
	  "actions": null,
	  "categories": null,
	  "file": null
	}