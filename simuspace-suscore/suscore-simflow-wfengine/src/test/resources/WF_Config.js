{
  "elements": [{
    "name": "I-O",
    "key": "wfe_io",
    "description": "",
    "comments": "",
    "allowedFieldKeys": ["section", "text", "select", "textarea", "integer", "boolean", "float", "file", "directory", "object"],
    "fields": [{
      "mode": "template",
      "title": "User Input Parameters",
      "subtitle": "Click add button ",
      "type": "section",
      "name": "section_input_parameters"
    }],
    "ui": {
      "icon": {
        "image": "images/wf/sign-in-single.png",
        "tooltip": "I/O"
      },
      "node": [{
        "selector": ".wfe_io",
        "css": {
          "shape": "octagon",
          "content": "data(name)",
          "background-color": "#32c5d2",
          "background-image": "images/wf/sign-in.svg"
        }
      }, {
        "selector": ".wfe_io.field-error",
        "css": {
          "background-color": "#CA5E58"
        }
      }]
    },
    "isInternal": true,
    "isActive": true,
    "runMode": null,
    "categories": [{
      "name": "element"
    }],
    "info": {
      "version": "0.0.1",
      "description": "A workflow element which provides a list of possible user-filled inputs, and generates outputs which have an outgoing connection to <applicable> to be used as input at <applicable>",
      "comments": "Specification developed by @Huzaifah"
    }
  }, {
    "name": "Execute",
    "key": "wfe_script",
    "description": "",
    "comments": "",
    "allowedFieldKeys": [],
    "fields": [{
      "mode": "template",
      "title": "User Script",
      "type": "section",
      "name": "section_user_script"
    }, {
      "mode": "template",
      "type": "select",
      "name": "syntax",
      "value": "Python",
      "label": "Script Syntax",
      "options": ["bash", "bat"],
      "rules": {
        "required": true
      },
      "multiple": false
    }, {
      "mode": "template",
      "name": "script",
      "value": "",
      "label": "User Script",
      "type": "textarea",
      "fullScreen": true,
      "rules": {
        "required": true
      },
      "multiple": false
    }],
    "ui": {
      "icon": {
        "image": "images/wf/code-single.png",
        "tooltip": "Execute"
      },
      "node": [{
        "selector": ".wfe_script",
        "css": {
          "shape": "rectangle",
          "content": "data(name)",
          "background-color": "#555",
          "background-image": "images/wf/code.svg"
        }
      }, {
        "selector": ".wfe_script.field-error",
        "css": {
          "background-color": "#CA5E58"
        }
      }]
    },
    "isInternal": true,
    "isActive": true,
    "runMode": "client",
    "categories": [{
      "name": "element"
    }],
    "info": {
      "version": "0.0.1",
      "description": "A workflow element which provides a list of possible user-filled inputs, and generates outputs which have an outgoing connection to <applicable> to be used as input at <applicable>",
      "comments": "Specification developed by @Huzaifah"
    }
  }, {
    "name": "SuSFile",
    "key": "wfe_sus_file",
    "description": "",
    "comments": "",
    "allowedFieldKeys": [],
    "fields": [{
      "mode": "template",
      "title": "SuSFile",
      "type": "section",
      "name": "section_SuSFile"
    }, {
      "mode": "template",
      "type": "select",
      "name": "syntax",
      "value": "Download",
      "label": "Script Syntax",
      "options": [
        "Download"
      ],
      "rules": {
        "required": true
      },
      "multiple": false
    }, {
      "mode": "template",
      "name": "script",
      "value": "",
      "label": "User Script",
      "type": "textarea",
      "rules": {
        "required": true
      },
      "multiple": false
    }],
    "ui": {
      "icon": {
        "image": "images/wf/fa-cloud-download.png",
        "tooltip": "SuSFile"
      },
      "node": [{
        "selector": ".wfe_sus_file",
        "css": {
          "shape": "circle",
          "content": "data(name)",
          "background-color": "#C49F47",
          "background-image": "images/wf/fa-cloud-download.svg"
        }
      }, {
        "selector": ".wfe_sus_file.field-error",
        "css": {
          "background-color": "#CA5E58"
        }
      }]
    },
    "isInternal": true,
    "isActive": true,
    "runMode": "client",
    "categories": [{
      "name": "element"
    }],
    "info": {
      "version": "0.0.1",
      "description": "A workflow element which provides a list of possible user-filled inputs, and generates outputs which have an outgoing connection to <applicable> to be used as input at <applicable>",
      "comments": "Specification developed by @Huzaifah"
    }
  }, {
    "name": "Rectangle",
    "key": "wfe_shape",
    "description": "",
    "comments": "",
    "allowedFieldKeys": [],
    "fields": [],
    "ui": {
      "icon": {
        "image": "images/wf/square.png",
        "tooltip": "Free Shape"
      },
      "node": [{
        "selector": ".wfe_shape",
        "css": {
          "shape": "square",
          "content": "data(name)",
          "border-style": "solid",
          "border-width": 2,
          "border-color": "green",
          "opacity": 0.1,
          "z-index": 1
        }
      }, {
        "selector": ".wfe_shape.field-error",
        "css": {
          "background-color": "#CA5E58"
        }
      }]
    },
    "isInternal": true,
    "isFreeShape": true,
    "isResizable": true,
    "isActive": true,
    "runMode": "client",
    "categories": [{
      "name": "element"
    }],
    "info": {
      "version": "0.0.1",
      "description": "A workflow element which provides a list of possible user-filled inputs, and generates outputs which have an outgoing connection to <applicable> to be used as input at <applicable>",
      "comments": "Specification developed by @Huzaifah"
    }
  }, {
    "name": "Label",
    "key": "wfe_label",
    "description": "",
    "comments": "",
    "allowedFieldKeys": [],
    "fields": [{
      "mode": "template",
      "type": "textarea",
      "editable": false,
      "name": "label",
      "value": "Label",
      "label": "Label",
      "rules": {
        "required": true
      }
    }],
    "ui": {
      "icon": {
        "image": "images/wf/label.png",
        "tooltip": "Label"
      },
      "node": [{
        "selector": ".wfe_label",
        "css": {
          "shape": "roundrectangle",
          "background-color": "white",
          "background-opacity": 0.5,
          "color": "grey",
          "font-size": 20,
          "font-weight": "bold",
          "text-valign": "center",
          "text-halign": "center",
          "text-wrap": "wrap",
          "width": "label",
          "height": "label",
          "padding-left": "10px",
          "padding-right": "10px",
          "padding-top": "10px",
          "padding-bottom": "10px",
          "border-width": 1,
          "border-opacity": 0,
          "text-margin-y": 3,
          "z-index": 2
        }
      }, {
        "selector": ".wfe_label:selected",
        "css": {
          "border-width": 1,
          "border-opacity": 0.5
        }
      }, {
        "selector": ".wfe_label.field-error",
        "css": {
          "background-color": "#CA5E58"
        }
      }]
    },
    "isInternal": true,
    "isFreeShape": true,
    "isResizable": false,
    "isActive": true,
    "runMode": "client",
    "categories": [{
      "name": "element"
    }],
    "info": {
      "version": "0.0.1",
      "description": "A workflow element which provides a list of possible user-filled inputs, and generates outputs which have an outgoing connection to <applicable> to be used as input at <applicable>",
      "comments": "Specification developed by @Huzaifah"
    }
  }],
  "applicable": {
    "wfe_script": ["wfe_io"],
    "wfe_sus_file": ["wfe_io"],
    "wfe_io": ["wfe_script", "wfe_sus_file"]
  }
}