{
  "elements": [
    {
      "title": "IO",
      "icon": "images/wf/sign-in-single.png",
      "element": {
        "data": {
          "name": "I-O",
          "key": "wfe_io",
          "description": "",
          "comments": "",
          "allowedConnections": [
            "wfe_script",
            "wfe_export_files",
            "wfe_condition",
            "wfe_wait"
          ],
          "allowedFieldKeys": [
            "section",
            "divider",
            "text",
            "select",
            "textarea",
            "divider",
            "integer",
            "float",
            "boolean",
            "divider",
            "os-file",
            "os-directory",
            "divider",
            "object"
          ],
          "exceptions": [
            {
              "mode": "template",
              "type": "text",
              "label": "Maximum Execution Time (seconds)",
              "value": -1,
              "name": "max-execution-time",
              "integer": true,
              "rules": {
                "required": true,
                "integer": true,
                "maxlength": 64
              }
            }
          ],
          "fields": [],
          "isInternal": true,
          "isActive": true,
          "runMode": null,
          "categories": [
            {
              "name": "element"
            }
          ],
          "info": {
            "version": "0.0.1",
            "description": "",
            "comments": ""
          }
        },
        "style": [
          {
            "selector": ".wfe_io",
            "css": {
              "shape": "octagon",
              "content": "data(name)",
              "background-color": "#32c5d2",
              "background-image": "images/wf/sign-in.svg"
            }
          },
          {
            "selector": ".wfe_io.field-error",
            "css": {
              "background-color": "#CA5E58"
            }
          }
        ]
      }
    },
    {
      "title": "Execute",
      "icon": "images/wf/code-single.png",
      "element": {
        "data": {
          "name": "Execute",
          "key": "wfe_script",
          "description": "",
          "comments": "",
          "allowedFieldKeys": [],
          "allowedConnections": [
            "wfe_io",
            "wfe_condition",
            "wfe_wait",
            "wfe_export_dataobject",
            "wfe_import_dataobject",
            "wfe_import_objects"
          ],
          "exceptions": [
            {
              "mode": "template",
              "type": "text",
              "label": "Maximum Execution Time (seconds)",
              "value": -1,
              "name": "max-execution-time",
              "integer": true,
              "rules": {
                "required": true,
                "integer": true,
                "maxlength": 64
              }
            }
          ],
          "fields": [
            {
              "mode": "template",
              "type": "select",
              "name": "syntax",
              "value": "Python",
              "label": "Script Syntax",
              "options": [
                "bash",
                "bat"
              ],
              "rules": {
                "required": true
              },
              "multiple": false,
              "sortable": false,
              "editable": false
            },
            {
              "mode": "template",
              "name": "script",
              "value": "",
              "label": "User Script",
              "type": "textarea",
              "fullScreen": true,
              "rules": {
                "required": true
              },
              "multiple": false,
              "sortable": false,
              "editable": false
            }
          ],
          "isInternal": true,
          "isActive": true,
          "runMode": "local",
          "categories": [
            {
              "name": "element"
            }
          ],
          "info": {
            "version": "0.0.1",
            "description": "",
            "comments": ""
          }
        },
        "style": [
          {
            "selector": ".wfe_script",
            "css": {
              "shape": "rectangle",
              "content": "data(name)",
              "background-color": "#555",
              "background-image": "images/wf/code.svg"
            }
          },
          {
            "selector": ".wfe_script.field-error",
            "css": {
              "background-color": "#CA5E58"
            }
          }
        ]
      }
    },
    {
      "title": "Wait",
      "icon": "images/wf/clock_face_three_oclock.png",
      "element": {
        "data": {
          "name": "Wait",
          "key": "wfe_wait",
          "description": "",
          "comments": "",
          "allowedFieldKeys": [],
          "allowedConnections": [
            "wfe_wait",
            "wfe_script",
            "wfe_export_files",
            "wfe_io",
            "wfe_condition"
          ],
          "exceptions": [
            {
              "mode": "template",
              "type": "text",
              "label": "Maximum Execution Time (seconds)",
              "value": -1,
              "name": "max-execution-time",
              "integer": true,
              "rules": {
                "required": true,
                "integer": true,
                "maxlength": 64
              }
            }
          ],
          "fields": [
            {
              "mode": "template",
              "editable": false,
              "name": "event",
              "value": "os-file",
              "label": "Event",
              "options": [
                "file",
                "time",
                "date",
                "file_unlocked"
              ],
              "multiple": false,
              "rules": {
                "required": true
              },
              "label": "Event",
              "sortable": false,
              "type": "select",
              "value": "file"
            },
            {
              "mode": "template",
              "variable-mode": false,
              "editable": false,
              "name": "eventValue",
              "rules": {
                "required": true
              },
              "label": "Value",
              "sortable": false,
              "type": "os-file",
              "value": ""
            }
          ],
          "isInternal": true,
          "isActive": true,
          "runMode": "local",
          "categories": [
            {
              "name": "element"
            }
          ],
          "info": {
            "version": "0.0.1",
            "description": "",
            "comments": ""
          }
        },
        "style": [
          {
            "selector": ".wfe_wait",
            "css": {
              "shape": "diamond",
              "content": "data(name)",
              "background-color": "#2B6729",
              "background-image": "images/wf/clock_face_three_oclock.svg"
            }
          },
          {
            "selector": ".wfe_wait.field-error",
            "css": {
              "background-color": "#CA5E58"
            }
          }
        ]
      }
    },
    {
      "title": "Condition",
      "icon": "images/wf/epsilon-de.png",
      "element": {
        "data": {
          "name": "Condition",
          "condition": true,
          "key": "wfe_condition",
          "description": "",
          "comments": "",
          "allowedFieldKeys": [],
          "allowedConnections": [
            "wfe_script",
            "wfe_export_files",
            "wfe_wait",
            "wfe_io",
            "wfe_condition"
          ],
          "exceptions": [
            {
              "mode": "template",
              "type": "text",
              "label": "Maximum Execution Time (seconds)",
              "value": -1,
              "name": "max-execution-time",
              "integer": true,
              "rules": {
                "required": true,
                "integer": true,
                "maxlength": 64
              }
            }
          ],
          "fields": [
            {
              "mode": "template",
              "name": "expression",
              "value": "",
              "label": "Expression",
              "editable": false,
              "variable-mode": false,
              "type": "text",
              "rules": {
                "required": true
              }
            },
            {
              "mode": "template",
              "type": "select",
              "name": "truePath",
              "picker": {
                "liveSearch": true
              },
              "value": "",
              "label": "True Path",
              "editable": false,
              "options": [],
              "multiple": true
            },
            {
              "mode": "template",
              "type": "checkbox",
              "name": "trueEndPath",
              "editable": false,
              "value": "",
              "label": "End True Path"
            },
            {
              "mode": "template",
              "type": "select",
              "name": "falsePath",
              "editable": false,
              "value": "",
              "picker": {
                "liveSearch": true
              },
              "label": "False Path",
              "options": [],
              "multiple": true
            },
            {
              "mode": "template",
              "type": "checkbox",
              "name": "falseEndPath",
              "editable": false,
              "value": "",
              "label": "End False Path"
            }
          ],
          "isInternal": true,
          "isActive": true,
          "runMode": "local",
          "categories": [
            {
              "name": "element"
            }
          ],
          "info": {
            "version": "0.0.1",
            "description": "",
            "comments": ""
          }
        },
        "style": [
          {
            "selector": ".wfe_condition",
            "css": {
              "shape": "diamond",
              "content": "data(name)",
              "background-color": "#4682B4",
              "background-image": "images/wf/epsilon-de.svg"
            }
          },
          {
            "selector": ".wfe_condition.field-error",
            "css": {
              "background-color": "#CA5E58"
            }
          }
        ]
      }
    }
  ]
}