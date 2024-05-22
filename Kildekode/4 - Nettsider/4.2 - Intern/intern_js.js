// Initialize fet   ching of slots and reservations
document.addEventListener('DOMContentLoaded', function() {
    slots = generateDaySlots(); // Generate all slots initially
    fetchReservations(); // Then fetch reservations to mark some slots as booked
    displaySlots(); // Display initially all slots as free until reservations are fetched
});

let slots = [];
// Set this dynamically as needed


// Generate time slots for a full day from 8 AM to 9 PM with 5-minute intervals
function generateDaySlots() {
    const startHour = 8;
    const endHour = 23;
    const interval = 5;
    let generatedSlots = [];
    for (let hour = startHour; hour < endHour; hour++) {
        for (let minute = 0; minute < 60; minute += interval) {
            let startMinute = minute < 10 ? `0${minute}` : minute;
            let endMinute = minute + interval;
            endMinute = endMinute >= 60 ? '00' : endMinute;
            let endTime = endMinute === '00' ? hour + 1 : hour;;
            //const formattedSeconds = '00'; 
            if (endMinute >= 60) {
                endMinute = '00';
                endTime = hour + 1;
            }
            generatedSlots.push({
                start: `${hour}:${startMinute}`,
                end: `${endTime}:${endMinute}`,
                status: 'free'
            });
        }
    }
    return generatedSlots;
}

async function fetchReservations() {
    try {
        const response = await fetch('http://10.100.101.61:8081/internalReservations', {
            method: 'GET',
            headers: { 'Accept': 'application/json' }
        });
        if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
        const reservations = await response.json();
        if (reservations.length > 0) {
            console.log("First reservation received:", reservations[0]);
            applyReservationsToSlots(reservations);
        } else {
            console.log("No reservations found.");
            // Optionally handle the empty reservations case here
        }
    } catch (error) {
        console.error('Failed to fetch reservations:', error);
    } finally {
        displaySlots();
    }
}

function applyReservationsToSlots(reservations) {
    reservations.forEach(reservation => {
        if (reservation.start && reservation.end) {
            let start = reservation.start; // Converts 'YYYY-MM-DDTHH:MM:SS' to 'HH:MM'
            let end = reservation.end;     // Same conversion as above

            slots.forEach(slot => {
                const check_startTime = formatTime(slot.start);
                const check_endTime = formatTime(slot.end);
                if ((check_startTime === start && check_endTime === end)) {
                    slot.status = 'booked';
                    console.log('booked', start);
                }
            });
        } else {
            console.log('Missing start or end for reservation:', reservation);
        }
    });
}




// Display slots dynamically on the webpage
function displaySlots() {
    const slotsContainer = document.getElementById('calendar');
    slotsContainer.innerHTML = '';
    slots.forEach(slot => {
        const slotElement = document.createElement('div');
        slotElement.textContent = `${slot.start} - ${slot.end} (${slot.status})`;
        slotElement.className = `slot ${slot.status}`;
        slotElement.onclick = function() {
            if (slot.status === 'free') {
                showBookingForm(slot);
            } else {
                alert('This slot is already booked!');
            }
        };
        slotsContainer.appendChild(slotElement);
    });
}


// Function to show booking form
function showBookingForm(slot) {
    const form = document.getElementById('calenderForm');
    // Ensure the time values are zero-padded to HH:mm format
    const startTime = formatTime(slot.start);
    const endTime = formatTime(slot.end);
    
    if (form) {

        const startInput = document.getElementById('start');
        const endInput = document.getElementById('end');
        if (startInput && endInput) {
            startInput.value = startTime;
            endInput.value = endTime;
        } else {
            console.error("Start or end input elements are missing.");
        }
    } else {
        console.error("Form element is missing.");
    }

    form.onsubmit = function (event) {
        event.preventDefault();
        bookSlot(startTime, endTime);
    };
    togglePopup();
}


// Helper function to format time string to HH:mm
function formatTime(timeStr) {
    const [hour, minute] = timeStr.split(':');
    const formattedHour = hour.length === 1 ? `0${hour}` : hour;
    const formattedMinute = minute.length === 1 ? `0${minute}` : minute;
    // Assume that seconds are not part of the original string and default to "00"
    const formattedSeconds = `00`;
    return `${formattedHour}:${formattedMinute}:${formattedSeconds}`;
}



// Assume this function toggles the visibility of a popup
function togglePopup() {
    const overlay = document.getElementById('popupOverlay');
    overlay.style.display = overlay.style.display === 'none' ? 'flex' : 'none';
}

async function bookSlot(startTime, endTime) {
    const body = {
        start: startTime,
        end: endTime
    };
    console.log('Sending POST request with body:', JSON.stringify(body)); // Log the request body
    try {
        const response = await fetch('http://10.100.101.61:8081/internalReservations', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(body)
        });

        if (response.ok) {
            const data = await response.json();
            console.log('Booking successful:', data);
            slot.status = 'booked'; // Mark the slot as booked on successful reservation
            return data;
        } else if (response.status === 404) {
            console.error('Error 404: Not Found. The endpoint does not exist.');
        } else {
            console.error(`HTTP error ${response.status}:`, await response.text());
        }
    } catch (error) {
        console.error('Network error:', error);
    }finally{
        displaySlots(); // Always display slots, regardless of booking success
    }
}
