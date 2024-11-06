const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

exports.notifyEventCreator = functions.firestore
    .document("cleanup_events/{eventId}/participants/{participantId}")
    .onCreate(async (snap, context) => {
        const eventId = context.params.eventId;
        
        // Get event document to fetch the creator's FCM token
        const eventDoc = await admin.firestore().collection("cleanup_events").doc(eventId).get();
        
        if (!eventDoc.exists) {
            console.log("Event document not found.");
            return;
        }

        const creatorToken = eventDoc.data().creatorToken;
        
        if (!creatorToken) {
            console.log("Creator's FCM token not found.");
            return;
        }

        const payload = {
            notification: {
                title: "New Volunteer Joined",
                body: "Someone joined your event!",
            },
        };

        // Send notification to the event creator
        try {
            await admin.messaging().sendToDevice(creatorToken, payload);
            console.log("Notification sent to creator");
        } catch (error) {
            console.error("Error sending notification", error);
        }
    });
