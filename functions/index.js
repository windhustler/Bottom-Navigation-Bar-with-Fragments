const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp();

exports.sendPush = functions.database.ref('/jobs/{jobId}').onCreate((snapshot, context) => {

const job = snapshot.val();

const hourlyPay = job.hourlyPay;
const email = job.email;
const jobDescription = job.jobDescription;
const jobLocation = job.jobLocation;
const phoneNumber = job.phoneNumber;


return loadUsers().then(users => {
	let tokens = [];
	for (let user of users) {
		tokens.push(user.deviceToken);
	}
	let payload = {
            data: {
                data_type: "direct_message",
                title: email,
                message: jobDescription,
            }
        };
	console.log ("tokens su: ", tokens);
	return admin.messaging().sendToDevice(token, payload)
						.then(function(response) {
							console.log("Successfully sent message:", response);
						  })
						  .catch(function(error) {
							console.log("Error sending message:", error);
						  });
});
	
		
});

function loadUsers() {
	let dbRef = admin.database().ref('/users/');
	console.log ("dbRef je: ", dbRef);
    let defer = new Promise((resolve, reject) => {
        dbRef.once('value', (snap) => {
            let data = snap.val();
            let users = [];
            for (var property in data) {
				console.log ("property je ", property);
				console.log ("console je: ", console);
				users.push(data[property]);
				console.log ("data[property] je: ", data[property]);
            }
            resolve(users);
        }, (err) => {
            reject(err);
        });
	});
	console.log ("defer je: ", defer);
	return defer;
	
}


