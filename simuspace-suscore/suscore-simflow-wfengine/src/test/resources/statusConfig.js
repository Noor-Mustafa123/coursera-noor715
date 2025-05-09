{ 
"statusConfiguration": [
    {
      "id": 1,
      "name": "WIP",
      "unique": true, 
      "moveOldVersionToStatus": 5,
      "canMoveToStatus": [85, 3, 5],
      "executable": true, 
      "visible": "owner" 
    }, {
      "id": 3,
      "name": "Approved",
      "unique": true,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [85, 5, 55],
      "executable": true,
      "visible": "all"
    }, {
      "id": 5,
      "name": "Invalid",
      "unique": false,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [85],
      "executable": true,
      "visible": "all"
    }, {
      "id": 55,
      "name": "Deprecated",
      "unique": true,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [85, 5],
      "executable": true,
      "visible": "all"
    }, {
      "id": 85,
      "name": "Deleted",
      "unique": false,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [],
      "executable": false,
      "visible": "none"
    }, {
      "id": 21,
      "name": "Pending",
      "unique": true,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [22, 25],
      "executable": false,
      "visible": "all"
    }, {
      "id": 22,
      "name": "Queued",
      "unique": true,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [20, 25],
      "executable": false,
      "visible": "all"
    }, {
      "id": 25,
      "name": "Aborted",
      "unique": true,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [],
      "executable": false,
      "visible": "all"
    }, {
      "id": 20,
      "name": "Running",
      "unique": true,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [22, 23, 24],
      "executable": false,
      "visible": "all"
    }, {
      "id": 24,
      "name": "Failed",
      "unique": true,
      "moveOldVersionToStatus": null,
      "canMoveToStatus": [],
      "executable": false,
      "visible": "all"
    }, {
      "id": 23,
      "name": "Completed",
      "unique": true,
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