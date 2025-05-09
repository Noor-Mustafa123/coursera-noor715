{
  "elements": {
    "edges": [
      {
        "data": {
          "source": "9b020560-9c4b-11e6-99ae-f31918567553",
          "target": "9d8750b0-9c4b-11e6-99ae-f31918567553",
          "id": "44cc9850-0179-453a-a8a0-2e6929bf5f0f"
        },
        "options": {
          "position": {},
          "group": "edges",
          "removed": false,
          "selected": false,
          "selectable": true,
          "locked": false,
          "grabbable": true,
          "classes": ""
        }
      },
      {
        "data": {
          "source": "9d8750b0-9c4b-11e6-99ae-f31918567553",
          "target": "9c5fc0a0-9c4b-11e6-99ae-f31918567553",
          "id": "8678c286-83f4-440d-859b-070afa873333"
        },
        "options": {
          "position": {},
          "group": "edges",
          "removed": false,
          "selected": false,
          "selectable": true,
          "locked": false,
          "grabbable": true,
          "classes": ""
        }
      }
    ],
    "nodes": [
      {
        "data": {
          "name": "Input",
          "key": "wfe_io",
          "description": "A workflow element which provides a list of possible user-filled inputs, and generates outputs which have an outgoing connection to <applicable> to be used as input at <applicable>",
          "comments": "Specification developed by @Huzaifah",
          "info": {
            "version": "0.0.1",
            "description": "A workflow element which provides a list of possible user-filled inputs, and generates outputs which have an outgoing connection to <applicable> to be used as input at <applicable>",
            "comments": "Specification developed by @Huzaifah"
          },
          "fields": [
            {
              "mode": "template",
              "title": "User Input Parameters",
              "subtitle": "Click add button ",
              "type": "section"
            },
            {
              "type": "file",
              "mode": "user",
              "label": "File1",
              "name": "File1",
              "rules": {
                "required": "false"
              },
              "value": {
                "agent": "client",
                "type": "file",
                "path": "src/test/resources/test.txt",
                "docId": null,
                "location": 0
              }
            }
          ],
          "ui": {
            "icon": {
              "background-image": "images/wf/sign-in.png",
              "tooltip": "Input"
            },
            "node": [
              {
                "selector": "node[key = 'wfe_input']",
                "css": {
                  "shape": "octagon",
                  "content": "data(name)",
                  "background-color": "#32c5d2",
                  "background-image": "images/wf/sign-in.svg"
                }
              }
            ]
          },
          "isInternal": true,
          "isActive": true,
          "runMode": null,
          "categories": [
            {
              "name": "element"
            }
          ],
          "version": "0.0.1",
          "id": "9b020560-9c4b-11e6-99ae-f31918567553",
          "options": {
            "group": "nodes",
            "removed": false,
            "selected": false,
            "selectable": true,
            "locked": false,
            "grabbable": true,
            "classes": ""
          }
        }
      },
      {
        "data": {
          "name": "Output",
          "key": "wfe_io",
          "description": "A workflow element which provides a list of possible outputs, an incoming connection can generate this output, while a outgoing connection can use this as input",
          "comments": "Specification developed by @Huzaifah",
          "info": {
            "version": "0.0.1",
            "description": "A workflow element which provides a list of possible user-filled inputs, and generates outputs which have an outgoing connection to <applicable> to be used as input at <applicable>",
            "comments": "Specification developed by @Huzaifah"
          },
          "fields": [
            {
              "mode": "template",
              "title": "User Output Parameters",
              "subtitle": "Click add button ",
              "type": "section"
            },
            {
              "type": "file",
              "mode": "user",
              "label": "File2",
              "name": "File2",
              "rules": {
                "required": "false"
              },
              "value": {
                "agent": "client",
                "type": "file",
                "path": "src/test/resources/testEmpty.txt",
                "docId": null,
                "location": 0
              }
            }
          ],
          "ui": {
            "icon": {
              "background-image": "images/wf/sign-out-option.png",
              "tooltip": "Output"
            },
            "node": [
              {
                "selector": "node[key = 'wfe_output']",
                "css": {
                  "shape": "circle",
                  "content": "data(name)",
                  "background-color": "#e7505a",
                  "background-image": "images/wf/sign-out-option.svg"
                }
              }
            ]
          },
          "isInternal": true,
          "isActive": true,
          "runMode": null,
          "categories": [
            {
              "name": "element"
            }
          ],
          "id": "9c5fc0a0-9c4b-11e6-99ae-f31918567553",
          "options": {
            "group": "nodes",
            "removed": false,
            "selected": false,
            "selectable": true,
            "locked": false,
            "grabbable": true,
            "classes": ""
          }
        }
      },
      {
        "data": {
          "name": "Script",
          "key": "wfe_script",
          "description": "A workflow element which executes a script, by consuming inputs and producing outputs",
          "comments": "Specification developed by @Huzaifah",
          "allowedFieldKeys": [],
          "info": {
            "version": "0.0.1",
            "description": "A workflow element which provides a list of possible user-filled inputs, and generates outputs which have an outgoing connection to <applicable> to be used as input at <applicable>",
            "comments": "Specification developed by @Huzaifah"
          },
          "fields": [
            {
              "mode": "template",
              "title": "User Script",
              "type": "section"
            },
            {
              "mode": "template",
              "type": "select",
              "name": "syntax",
              "value": "Python",
              "label": "Script Syntax",
              "options": [
                "bash",
                "bat",
                "Python",
                "Perl"
              ],
              "rules": {
                "required": true
              },
              "multiple": false
            },
            {
              "mode": "template",
              "name": "script",
              "value": "/bin/cp $Input.File1 $Output.File2",
              "label": "User Script",
              "type": "textarea",
              "rules": {
                "required": true
              },
              "multiple": false
            }
          ],
          "ui": {
            "icon": {
              "background-image": "images/wf/code.png",
              "tooltip": "Script"
            },
            "node": [
              {
                "selector": "node[key = 'wfe_script']",
                "css": {
                  "shape": "rectangle",
                  "content": "data(name)",
                  "background-color": "#555",
                  "background-image": "images/wf/code.svg"
                }
              }
            ]
          },
          "isInternal": true,
          "isActive": true,
          "runMode": "client",
          "categories": [
            {
              "name": "element"
            }
          ],
          "id": "9d8750b0-9c4b-11e6-99ae-f31918567553",
          "options": {
            "group": "nodes",
            "removed": false,
            "selected": true,
            "selectable": true,
            "locked": false,
            "grabbable": true,
            "classes": ""
          }
        }
      }
    ]
  },
  "id": "99e94260-9c4b-11e6-99ae-f31918567553",
  "name": "workflow-mashu",
  "createdBy": {
    "id": "9c5fc0a0-9c4b-11e6-99ae-f31918567553",
    "firstName": "Shumail Maqsood"
  },
  "comments": "",
  "description": ""
}