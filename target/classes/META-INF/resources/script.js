// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/** Fetches stats from the server and adds them to the page. */
  async function submitHandler() {
    const responseFromServer = await fetch('/register-handler');
    //const responseFromServer = await fetch('/register-handler');
    //const responseFromServer = await fetch('/register-handler');
    const textFromResponse = await responseFromServer.text();
    // The json() function returns an object that contains fields that we can
    // reference to create HTML.
    //const messages = await responseFromServer.json();
    //console.log(messages);

    //const jsonSize = Object.keys(messages).length;
    //var randomNumber = Math.floor(Math.random() * jsonSize); //Random number between
        //0 and 2 (inclusive).
    //var counter = 0; //Index to get to the random number.
    //var response = ""; //Random string of response.
    //var randomKey = Object.keys(messages)[randomNumber];
    //response = messages[randomKey];
  
    //const messagesElement = document.getElementById('messages-container');
    const messageElement = document.getElementById('message-container');
    //const formTarget = document.getElementsByTagName('form');

    if (textFromResponse != "") {
        messageElement.innerText = textFromResponse;
    }
    else {
        window.location.href="rightRegister.html";
    }
  }

  function validate(frm) {

    document.getElementById("message-container").innerHTML="";
    //Read form data:
    let username = frm.username.value;
    let password = frm.password.value;
    let confirmPassword = frm.confirmPassword.value;
    
    //Client side form validation logic:
    if(username == "") {
       document.getElementById("message-container").innerHTML =
      "<b>Username required.</b>";
       frm.username.focus(); // focus the text box
       return false;
    }

    if(password == "") {
        document.getElementById("message-container").innerHTML =
        "<b>Password required.</b>";
        frm.password.focus(); // focus the text box
        return false;
    }

    if(confirmPassword == "") {
        document.getElementById("message-container").innerHTML =
        "<b>Password confirmation required.</b>";
        frm.confirmPassword.focus(); // focus the text box
        return false;
    }

    return true;
    // true => form is error free
    // false => form validation errors
 }