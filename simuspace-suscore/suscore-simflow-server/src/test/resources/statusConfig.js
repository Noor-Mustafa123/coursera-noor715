{ 
"statusConfiguration": [
    {
      "id": 1,
      "name": "WIP",
      "unqiue": true, 
      "moveOldVersionToStatus": 5,
      "canMoveToStatus": [85, 3, 5],
      "executable": true, 
      "visible": "owner" 
    }, {
      "id": 3,
      "name": "Approved",
      "unqiue": true,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [85, 5, 55],
      "executable": true,
      "visible": "all"
    }, {
      "id": 5,
      "name": "Invalid",
      "unqiue": true,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [85],
      "executable": false,
      "visible": "all"
    }, {
      "id": 55,
      "name": "Deprecated",
      "unqiue": true,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [85, 5],
      "executable": false,
      "visible": "all"
    }, {
      "id": 85,
      "name": "Deleted",
      "unqiue": false,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [],
      "executable": false,
      "visible": "none"
    }, {
      "id": 21,
      "name": "Pending",
      "unqiue": true,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [22, 25],
      "executable": false,
      "visible": "all"
    }, {
      "id": 22,
      "name": "Queued",
      "unqiue": true,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [20, 25],
      "executable": false,
      "visible": "all"
    }, {
      "id": 25,
      "name": "Aborted",
      "unqiue": true,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [],
      "executable": false,
      "visible": "all"
    }, {
      "id": 20,
      "name": "Running",
      "unqiue": true,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [22, 23, 24],
      "executable": false,
      "visible": "all"
    }, {
      "id": 24,
      "name": "Failed",
      "unqiue": true,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [],
      "executable": false,
      "visible": "all"
    }, {
      "id": 23,
      "name": "Completed",
      "unqiue": true,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [],
      "executable": false,
      "visible": "all"
    }
  ],
  "applicable": [
    {   
      "validStatusList": [3],
      "defaultStatus": 3, 
      "type": "default"
    }, {
      "validStatusList": [20, 21, 22, 23, 24, 25],
      "defaultStatus": 20,
      "type": "Job"
    }, {
      "validStatusList": [1, 3, 5, 55, 85],
      "defaultStatus": 1,
      "type": "Workflow"
    }

  ]

}