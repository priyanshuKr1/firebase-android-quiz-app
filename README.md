# Quizzer

![Project Image](app/src/main/res/mipmap-hdpi/ic_launcher_foreground.png)

An android quiz app for conducting quizzes and easy management of results for universitiy/college students.

## Overview

An app to take tests and evaluate students by admins. 
Quizzer uses Google's firebase for managing the tests, results and authentication. 
Quizzer lets you see the leaderboard after successful completion of tests. 
Admin has access to all the results and other features automatically. 
Quizzer uses firebase-storage feature to save user profile images. 
Upload quizzes being created by admin in JSON stracture.

## Features

- Firebase authorization
- Timer bound tests
- Leaderboard after successful completion of test
- Also, user can see detailed solution after taking test
- Admin has access to all the results and user-profiles 
- Admin can download the detailed result in excel format
- user can chat with other users to discuss the doubts
- admins can create and upload test to firebase-database
- Firebase push notification management

## Screenshots

Screenshots of ui and setting-up of firebase [link](Screenshots)

# How To Use

### Set-up firebase project
 Create firebase project [here](https://console.firebase.google.com/).
 Paste the **google-services.json** to app folder. For help
 [Refer to firebase-docs](https://firebase.google.com/docs/android/setup) 
 
 ### Enable authentication
 Refer [here](https://firebase.google.com/docs/auth/) for firebase-docs.
 
 ### Create firebase realtime-database
Realtime-database consists of 5 childs, all childs except **admins** and **tests** are created manually in root of realtime-database, 
as shown below.
#### ![database-image](Screenshots/ss20.PNG)

### Creating tests or uploading tests
To upload tests, goto specifed link as specified below enter test name and import **JSON** feature to upload [json](TestQuestionTemplate.json) file. Also, it can be edited with text-editor with your choice of questions & answers.

#### ![database-test-image](Screenshots/ss23.png)

### Adding admins
Craete a child **admins** in root of realtime-database and copy the USER-UID from authentication tab, put **NAME = USER-UID** and **VALUE = true** see below for refrence.

#### ![admins-database](Screenshots/ss24.png)

#### Database rules
To manage users marks, for not re-update of marks after giving tests, [database rules](Rules.txt) paste the contents to database-rules section.

#### ![rules](Screenshots/ss25.png)

#### Click this [link](https://drive.google.com/file/d/1ALvOO9Zzr-dw0JftxILlrzH3yj9cBDJP/view?usp=sharing) to view prototype
