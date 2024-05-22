// functions that have been created by using a tutorial will have the appropriate link 

let auth0Client = null; // https://auth0.com/docs/quickstart/spa/vanillajs/01-login
let allReservations = null;

// https://auth0.com/docs/quickstart/spa/vanillajs/01-login
const fetchAuthConfig = () => fetch("/config/auth_config.json");

// https://auth0.com/docs/quickstart/spa/vanillajs/01-login
// https://auth0.com/docs/quickstart/spa/vanillajs/02-calling-an-api
const configureClient = async () => {
  const response = await fetchAuthConfig();
  const config = await response.json();
  
  auth0Client = await auth0.createAuth0Client({
    domain: config.domain,
    clientId: config.clientId,
    authorizationParams: {
      audience: config.audience
    }
  });
};

// https://auth0.com/docs/quickstart/spa/vanillajs/01-login
// this method is in part created by using the login-tutorial provided by auth0 and by self-writtten code
window.onload = async () => {
    await configureClient(); // wait until auth0-client is configured 

    const noVNClink = document.getElementById('noVNC_link'); // Stores the button element that redirects the user to the full-page NoVNC console
    const noVNCiframe = document.getElementById('noVNC_iframe'); // Stores the iframe element that embeds the NoVNC console in the page

    updateUI(); // render the page (based on if user is authenticated or not)
 
    const isAuthenticated = await auth0Client.isAuthenticated(); // check if the user is authenticated

    if (isAuthenticated) {
      
      const token = await auth0Client.getTokenSilently(); // get access token

      const userEmail = await auth0Client.getUser().then(getUserEmail => getUserEmail["email"]); // get user email

      updateUI(userEmail); // now that user is logged in, we can pass the obtained email to the "update-ui"-method

      const ticket = "PVEAuthCookie"; // Creates a variable that will store the value from the 'cookie' column in /credentials. Sets the prefix 'PVEAuthCookie'

      fetch(`/credentials?email=${userEmail}`, { // Fetches the contents on /credentials with a GET request, making sure to pass the Authorization header
    method: 'GET',
    headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json"
    }
}).then(response => {
    // Checks for succesful request
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    // Parsing the response
    return response.json();
}).then(data => {
    // Handle the JSON data
    const shift_val = 114; // Creates a shift value, that pushes the value of the VM id from 1 to xx, where xx is the start of the range in which the student VMs id start
    const cookie = data.cookie; // Stores the content of the 'cookie', 'token' and 'virtualMachineId' column in variables
    const toke = data.token;
    const vm_id = data.virtualMachineId;
    const fin = ticket.concat("=", cookie); // Concatenates ticket and cookie variables into fin variable, which is stored as a cookie to authorize the user
    document.cookie = fin;
    console.log('VM ID:', vm_id); // prints to console for debugging purposes
    document.getElementById("noVNC_link").classList.remove("hidden"); // Makes the NoVNC embedded console and button visible
    document.getElementById("noVNC_iframe").classList.remove("hidden");
    const noVNC_URL = "/novnc/?console=kvm&novnc=1&vmid=" + encodeURIComponent(vm_id + shift_val) + "&vmname=Student-VM&node=pve&resize=off&cmd=&csrfptoken=" + encodeURIComponent(toke); // Creates the URL for the NoVNC console, from the variables
    noVNClink.href = noVNC_URL; // Sets the source and href of the button and iframe to the URL
    noVNCiframe.src = noVNC_URL;
}).catch(error => {
    // Checks for errors during request
    console.error('Fetch error:', error);
});
      //getReservations(token, userEmail);

      // https://simonplend.com/how-to-use-fetch-to-post-form-data-as-json-to-your-api/ -> guide to creating a request using the fetch-api
      // https://www.youtube.com/watch?v=DqyJFV7QJqc&t=324s -> tutorial to creating a request using the fetch-api

      const calenderForm = document.getElementById("calenderForm"); // grab element by ID so that we can create specific behaviour when form is submitted

      // method for defining what to do when form is submitted
      calenderForm.addEventListener("submit", event => {

      // prevent default behaviour like page refreshing
        event.preventDefault();
        
        console.log(userEmail);

        const formData = new FormData(calenderForm); // obtain the entered data
        const data = Object.fromEntries(formData); // turn data into object
        
      // use the fetch-API to send request towards our API, this endpoint uses the query-parameter "userEmail"
        fetch(`/reservations?email=${userEmail}`, {
          method: 'POST', // http-method is post
          headers: {
            Authorization: `Bearer ${token}`, // send the access token so API will process our request
            "Content-Type": "application/json" // specify the content-type
          },
          body: JSON.stringify(data), // turn the object we want to send into JSON
        }).then(result => result.json()) // after request we want to turn received data into JSON
        .then(data => console.log(data)) // logging received data to console
        .catch(error => console.log(error)) // in case of error, show in console
        location.reload(); // finally, refresh the page so that the reservation can be displayed in the calendar 
      }); 

      // when the page is loaded and the user is logged in, try to fetch its reservations
      document.addEventListener('DOMContentLoaded', getReservations(token, userEmail));
      console.log(token);
      return;
    }
  
    // https://auth0.com/docs/quickstart/spa/vanillajs/01-login
    // this method is from the login-tutorial linked above
    // check for the code and state parameters
    const query = window.location.search;
    if (query.includes("code=") && query.includes("state=")) {
  
      // Process the login state
      await auth0Client.handleRedirectCallback();

      updateUI();
  
      // Use replaceState to redirect the user away and remove the querystring parameters
      window.history.replaceState({}, document.title, "/");

      location.reload();

    }
}

// https://auth0.com/docs/quickstart/spa/vanillajs/01-login
// this method is called every time the window refreshes 
const updateUI = async (mail) => {

    // check if the user has logged in or not
    const isAuthenticated = await auth0Client.isAuthenticated();
    
  // if-else-statements renderes the page based on if the user is logged in or not
    if (isAuthenticated) {
      document.getElementById("gated-content").classList.remove("hidden"); // reveals hidden content (calendar)
      document.getElementById("btn-logout").classList.remove("hidden"); // reveals logout-button
      document.getElementById("btn-login").classList.add("hidden"); // hides the login-button
      document.getElementById("top-text").innerHTML = `Reservasjonsoversikt for "${mail}"`; // add text on top of calendar showing the user email
    } else {
      document.getElementById("gated-content").classList.add("hidden"); // hides content (calendar)
      document.getElementById("btn-login").classList.remove("hidden"); // reveals login-button
      document.getElementById("btn-logout").classList.add("hidden"); //hides logout-button
      document.getElementById("btn-api").classList.add("hidden"); // hides the "delete-reservation"-button, this button will only be available if the user has an existing reservation
    }
};

// https://auth0.com/docs/quickstart/spa/vanillajs/01-login
// this method gets called when "login"-button is clicked
const login = async () => {
    await auth0Client.loginWithRedirect({
      authorizationParams: {
        redirect_uri: window.location.origin // redirects user back to initial window, but functionality that requires an authenticated user is now visible
      }
    });
};

// https://auth0.com/docs/quickstart/spa/vanillajs/01-login
// this method gets called when "logout"-button is clicked
const logout = () => {
  auth0Client.logout({
    logoutParams: {
      returnTo: window.location.origin // redirects user back to the previous page, but functionality that requires an authenticated user will be removed
    }
  });
};





/* Open when someone clicks on the span element */
function openNav() {
    document.getElementById("menu__toggle").style.width = "100%";
    
}
  
function closeNav() {
    document.getElementById('menu__box').style.width = "0%";
    document.getElementById('menu__toggle').checked = false; // Uncheck the toggle
}





/*Oppsett for kalender*/ 
async function generateCalender(userFound){
  
  console.log(allReservations);
  var calendarEl = document.getElementById('calendar'); 

  // if there are reservations related to the user, this if-statement will be executed
  if(userFound == true){
    var calendar = new FullCalendar.Calendar(calendarEl, { // Makes a FullCalendar object with the configuration paramateres under. https://fullcalendar.io/docs The documents for this code
      customButtons: { // Creates a custom button that will open the reservation form if pushed
        reserverVM: {
        text: 'Reserver',
        click: function() {
            const overlay = document.getElementById('popupOverlay'); 
            overlay.classList.toggle('show'); 
          }
        }
      },
        initialView: 'timeGridWeek', // Sets the initial view, being a week view
        nowIndicator: true, // A line in the calendar that shows what time it is now
        allDaySlot: false, // Removes the whole-day booking slot
        weekNumberCalculation: 'ISO', // Sets the standard for week numbers and dates
        slotMinTime: '08:00:00', // Start time for the calendar
        slotMaxTime: '21:00:00', // End time for the calendar
        weekends: false, // Hides weekends
        locale: 'no', // Sets the locale to norway
        headerToolbar: { // Sets 3 sections on the header bar, on the left are the buttons: previous day/week, next day/week, current day/week, and the custom reservation button
          left: 'prev,next today,reserverVM',
          center: 'title', // The middle only has the title of the calendar
          right: 'timeGridWeek,timeGridDay' // The right section has two buttons for choosing week view or day view
        },
        navLinks: true, // can click day/week names to navigate views
        dayMaxEvents: true, // allow "more" link when too many events
        events: [allReservations] // allReservations will contain reservation and it will be displayed
      });
  } 
  // if there are NO reservations related to the user, this else-statement will be executed
  else{
    var calendar = new FullCalendar.Calendar(calendarEl, {
      customButtons: {
        reserverVM: {
        text: 'Reserver',
        click: function() {
            const overlay = document.getElementById('popupOverlay'); 
            overlay.classList.toggle('show'); 
          }
        }
      },
        initialView: 'timeGridWeek',
        nowIndicator: true,
        allDaySlot: false,
        weekNumberCalculation: 'ISO',
        slotMinTime: '08:00:00',
        slotMaxTime: '21:00:00',
        weekends: false,
        locale: 'no',
        headerToolbar: {
          left: 'prev,next today,reserverVM',
          center: 'title',
          right: 'timeGridWeek,timeGridDay'
        },
        navLinks: true, // can click day/week names to navigate views
        dayMaxEvents: true, // allow "more" link when too many events
        events: allReservations // allReservations will be null and will not be displayed
      });
  }
    calendar.render(); // Renders the calendar after the configuration parameters has been set
  }

  
  function on() { // Displays the form to be used for booking time
    document.getElementById("myForm").style.display = "block";
  }
  
  function off() { // Hides the form to be used for booking time
    document.getElementById("myForm").style.display = "none";
  }
  
  function togglePopup() { 
    const overlay = document.getElementById('popupOverlay'); 
    overlay.classList.toggle('show'); 
  }
  

// function used to get the reservations for the logged in user, takes two parameters: access token and email
async function getReservations(token, userEmail) {

  // use fetch-API to make request to our API, send email as query-parameter
  const response = await fetch(`/reservations?email=${userEmail}`, {
    headers: {
      Authorization: `Bearer ${token}`, // add access token so API will process the request
    }
  });

  // if the response goes not get any reservations related to the user
  if(!response.ok){
    document.getElementById("btn-api").classList.add("hidden"); // remove "delete reservation"-button
    console.log("User has no reservations");
    generateCalender(false); // generate an empty calendar 
  } 
  // if the response does get reservations related to the user
  else {
    const data = await response.json(); // transfer response to JSON-format
    document.getElementById("btn-api").classList.remove("hidden"); // make "delete reservation"-button visible
    allReservations = data; // update global variable "allReservations" with the received reservations
    generateCalender(true); // generate calendar with the retrieved reservations
  }
}

 // method for deleting a reservation
const deleteReservation = async () => {
    try {

      // get access token, this is needed in order to use the API
      const token = await auth0Client.getTokenSilently();

      // get the users email, this is extracted from the ID-token
      const userEmail = await auth0Client.getUser().then(getUserEmail => getUserEmail["email"]);

      // use fetch-api to call the external API
      // the API needs the users email and the authorization header using the access token
      const response = await fetch(`/reservations?email=${userEmail}`, {
        headers: {
          "Authorization": `Bearer ${token}`
        },
        method: 'DELETE', // http-method is delete
      });

      location.reload(); // refresh the page
    
    } catch (e) {
        // Display errors in the console
        console.error(e);
    }
}

