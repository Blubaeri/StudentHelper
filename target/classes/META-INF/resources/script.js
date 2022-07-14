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
  async function getMessages() {
    //const responseFromServer = await fetch('/hello');
    const responseFromServer = await fetch('/register-handler');
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
    const warningElement = document.getElementById('warning-container');
    warningElement.innerText = responseFromServer;
  }