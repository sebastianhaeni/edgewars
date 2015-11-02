(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){

function load() {
    $.ajax({
        url: '/data/data.json',
        success: function(response){
            process(response);
        }
    })
}

function process(json) {
    byTask(json);
    bySprint(json);
    byCategory(json);
    byPerson(json);
}

function byTask(json) {
    $.each(json.cards, function(i, card) {
        /*if(card.closed !== true) {
            return true;
        }*/
        var idShort = card.idShort;

        $.each(json.actions, function(i, action) {
            if(!action.data.card) {
                return true;
            }
            if(action.data.card.idShort === idShort) {
                if(action.type !== 'commentCard') {
                    return true;
                }
                card.hours = action.data.text;
                return false;
            }
        });

        if(card.hours == undefined) {
            return true;
        }

        $('#hours-by-task').append('<tr><td>' + card.name + '</td><td>' + card.hours + '</td></tr>');
    });
}

function bySprint(json) {
    var sprints = {};
    $.each(json.cards, function(i, card) {
        /*if(card.closed !== true) {
            return true;
        }*/
        var idShort = card.idShort;

        $.each(json.actions, function(i, action) {
            if(!action.data.card) {
                return true;
            }
            if(action.data.card.idShort === idShort) {
                if(action.type !== 'commentCard') {
                    return true;
                }
                card.hours = action.data.text;
                return false;
            }
        });

        $.each(card.labels, function(i, label) {
            if(label.name.substring(0, 3) === 'Spr') {
                if(isNaN(sprints[label.name])) {
                    sprints[label.name] = 0;
                }
                sprints[label.name] += parseInt(card.hours);
            }
        });
    });

    $.each(sprints, function(name, hours) {
        $('#hours-by-sprint').append('<tr><td>' + name + '</td><td>' + hours + '</td></tr>');
    });
}

function byCategory(json) {
    var sprints = {};
    $.each(json.cards, function(i, card) {
        /*if(card.closed !== true) {
            return true;
        }*/
        var idShort = card.idShort;

        $.each(json.actions, function(i, action) {
            if(!action.data.card) {
                return true;
            }
            if(action.data.card.idShort === idShort) {
                if(action.type !== 'commentCard') {
                    return true;
                }
                card.hours = action.data.text;
                return false;
            }
        });

        $.each(card.labels, function(i, label) {
            if(label.name.substring(0, 3) !== 'Spr') {
                if(isNaN(sprints[label.name])) {
                    sprints[label.name] = 0;
                }
                sprints[label.name] += parseInt(card.hours);
            }
        });
    });

    $.each(sprints, function(name, hours) {
        $('#hours-by-category').append('<tr><td>' + name + '</td><td>' + hours + '</td></tr>');
    });
}

function byPerson(json) {
    var persons = {};
    $.each(json.cards, function(i, card) {
        /*if(card.closed !== true) {
            return true;
        }*/
        var idShort = card.idShort;

        $.each(json.actions, function(i, action) {
            if(!action.data.card) {
                return true;
            }
            if(action.data.card.idShort === idShort) {
                if(action.type !== 'commentCard') {
                    return true;
                }
                card.hours = action.data.text;
                return false;
            }
        });

        $.each(json.members, function(i, member) {
            if(card.idMembers.length <= 0){
                return true;
            }

            if(member.id == card.idMembers[0]) {
                if(isNaN(persons[member.fullName])) {
                    persons[member.fullName] = 0;
                }
                persons[member.fullName] += parseInt(card.hours);
            }
        });
    });

    $.each(persons, function(name, hours) {
        $('#hours-by-person').append('<tr><td>' + name + '</td><td>' + hours + '</td></tr>');
    });
}

load();

},{}]},{},[1])
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIm5vZGVfbW9kdWxlcy9icm93c2VyaWZ5L25vZGVfbW9kdWxlcy9icm93c2VyLXBhY2svX3ByZWx1ZGUuanMiLCJhcHAvc2NyaXB0cy9hcHAuanMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUE7QUNBQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EiLCJmaWxlIjoiZ2VuZXJhdGVkLmpzIiwic291cmNlUm9vdCI6IiIsInNvdXJjZXNDb250ZW50IjpbIihmdW5jdGlvbiBlKHQsbixyKXtmdW5jdGlvbiBzKG8sdSl7aWYoIW5bb10pe2lmKCF0W29dKXt2YXIgYT10eXBlb2YgcmVxdWlyZT09XCJmdW5jdGlvblwiJiZyZXF1aXJlO2lmKCF1JiZhKXJldHVybiBhKG8sITApO2lmKGkpcmV0dXJuIGkobywhMCk7dmFyIGY9bmV3IEVycm9yKFwiQ2Fubm90IGZpbmQgbW9kdWxlICdcIitvK1wiJ1wiKTt0aHJvdyBmLmNvZGU9XCJNT0RVTEVfTk9UX0ZPVU5EXCIsZn12YXIgbD1uW29dPXtleHBvcnRzOnt9fTt0W29dWzBdLmNhbGwobC5leHBvcnRzLGZ1bmN0aW9uKGUpe3ZhciBuPXRbb11bMV1bZV07cmV0dXJuIHMobj9uOmUpfSxsLGwuZXhwb3J0cyxlLHQsbixyKX1yZXR1cm4gbltvXS5leHBvcnRzfXZhciBpPXR5cGVvZiByZXF1aXJlPT1cImZ1bmN0aW9uXCImJnJlcXVpcmU7Zm9yKHZhciBvPTA7bzxyLmxlbmd0aDtvKyspcyhyW29dKTtyZXR1cm4gc30pIiwiXHJcbmZ1bmN0aW9uIGxvYWQoKSB7XHJcbiAgICAkLmFqYXgoe1xyXG4gICAgICAgIHVybDogJy9kYXRhL2RhdGEuanNvbicsXHJcbiAgICAgICAgc3VjY2VzczogZnVuY3Rpb24ocmVzcG9uc2Upe1xyXG4gICAgICAgICAgICBwcm9jZXNzKHJlc3BvbnNlKTtcclxuICAgICAgICB9XHJcbiAgICB9KVxyXG59XHJcblxyXG5mdW5jdGlvbiBwcm9jZXNzKGpzb24pIHtcclxuICAgIGJ5VGFzayhqc29uKTtcclxuICAgIGJ5U3ByaW50KGpzb24pO1xyXG4gICAgYnlDYXRlZ29yeShqc29uKTtcclxuICAgIGJ5UGVyc29uKGpzb24pO1xyXG59XHJcblxyXG5mdW5jdGlvbiBieVRhc2soanNvbikge1xyXG4gICAgJC5lYWNoKGpzb24uY2FyZHMsIGZ1bmN0aW9uKGksIGNhcmQpIHtcclxuICAgICAgICAvKmlmKGNhcmQuY2xvc2VkICE9PSB0cnVlKSB7XHJcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgIH0qL1xyXG4gICAgICAgIHZhciBpZFNob3J0ID0gY2FyZC5pZFNob3J0O1xyXG5cclxuICAgICAgICAkLmVhY2goanNvbi5hY3Rpb25zLCBmdW5jdGlvbihpLCBhY3Rpb24pIHtcclxuICAgICAgICAgICAgaWYoIWFjdGlvbi5kYXRhLmNhcmQpIHtcclxuICAgICAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGlmKGFjdGlvbi5kYXRhLmNhcmQuaWRTaG9ydCA9PT0gaWRTaG9ydCkge1xyXG4gICAgICAgICAgICAgICAgaWYoYWN0aW9uLnR5cGUgIT09ICdjb21tZW50Q2FyZCcpIHtcclxuICAgICAgICAgICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIGNhcmQuaG91cnMgPSBhY3Rpb24uZGF0YS50ZXh0O1xyXG4gICAgICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfSk7XHJcblxyXG4gICAgICAgIGlmKGNhcmQuaG91cnMgPT0gdW5kZWZpbmVkKSB7XHJcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgIH1cclxuXHJcbiAgICAgICAgJCgnI2hvdXJzLWJ5LXRhc2snKS5hcHBlbmQoJzx0cj48dGQ+JyArIGNhcmQubmFtZSArICc8L3RkPjx0ZD4nICsgY2FyZC5ob3VycyArICc8L3RkPjwvdHI+Jyk7XHJcbiAgICB9KTtcclxufVxyXG5cclxuZnVuY3Rpb24gYnlTcHJpbnQoanNvbikge1xyXG4gICAgdmFyIHNwcmludHMgPSB7fTtcclxuICAgICQuZWFjaChqc29uLmNhcmRzLCBmdW5jdGlvbihpLCBjYXJkKSB7XHJcbiAgICAgICAgLyppZihjYXJkLmNsb3NlZCAhPT0gdHJ1ZSkge1xyXG4gICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICB9Ki9cclxuICAgICAgICB2YXIgaWRTaG9ydCA9IGNhcmQuaWRTaG9ydDtcclxuXHJcbiAgICAgICAgJC5lYWNoKGpzb24uYWN0aW9ucywgZnVuY3Rpb24oaSwgYWN0aW9uKSB7XHJcbiAgICAgICAgICAgIGlmKCFhY3Rpb24uZGF0YS5jYXJkKSB7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBpZihhY3Rpb24uZGF0YS5jYXJkLmlkU2hvcnQgPT09IGlkU2hvcnQpIHtcclxuICAgICAgICAgICAgICAgIGlmKGFjdGlvbi50eXBlICE9PSAnY29tbWVudENhcmQnKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICBjYXJkLmhvdXJzID0gYWN0aW9uLmRhdGEudGV4dDtcclxuICAgICAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH0pO1xyXG5cclxuICAgICAgICAkLmVhY2goY2FyZC5sYWJlbHMsIGZ1bmN0aW9uKGksIGxhYmVsKSB7XHJcbiAgICAgICAgICAgIGlmKGxhYmVsLm5hbWUuc3Vic3RyaW5nKDAsIDMpID09PSAnU3ByJykge1xyXG4gICAgICAgICAgICAgICAgaWYoaXNOYU4oc3ByaW50c1tsYWJlbC5uYW1lXSkpIHtcclxuICAgICAgICAgICAgICAgICAgICBzcHJpbnRzW2xhYmVsLm5hbWVdID0gMDtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIHNwcmludHNbbGFiZWwubmFtZV0gKz0gcGFyc2VJbnQoY2FyZC5ob3Vycyk7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9KTtcclxuICAgIH0pO1xyXG5cclxuICAgICQuZWFjaChzcHJpbnRzLCBmdW5jdGlvbihuYW1lLCBob3Vycykge1xyXG4gICAgICAgICQoJyNob3Vycy1ieS1zcHJpbnQnKS5hcHBlbmQoJzx0cj48dGQ+JyArIG5hbWUgKyAnPC90ZD48dGQ+JyArIGhvdXJzICsgJzwvdGQ+PC90cj4nKTtcclxuICAgIH0pO1xyXG59XHJcblxyXG5mdW5jdGlvbiBieUNhdGVnb3J5KGpzb24pIHtcclxuICAgIHZhciBzcHJpbnRzID0ge307XHJcbiAgICAkLmVhY2goanNvbi5jYXJkcywgZnVuY3Rpb24oaSwgY2FyZCkge1xyXG4gICAgICAgIC8qaWYoY2FyZC5jbG9zZWQgIT09IHRydWUpIHtcclxuICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgfSovXHJcbiAgICAgICAgdmFyIGlkU2hvcnQgPSBjYXJkLmlkU2hvcnQ7XHJcblxyXG4gICAgICAgICQuZWFjaChqc29uLmFjdGlvbnMsIGZ1bmN0aW9uKGksIGFjdGlvbikge1xyXG4gICAgICAgICAgICBpZighYWN0aW9uLmRhdGEuY2FyZCkge1xyXG4gICAgICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgaWYoYWN0aW9uLmRhdGEuY2FyZC5pZFNob3J0ID09PSBpZFNob3J0KSB7XHJcbiAgICAgICAgICAgICAgICBpZihhY3Rpb24udHlwZSAhPT0gJ2NvbW1lbnRDYXJkJykge1xyXG4gICAgICAgICAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgY2FyZC5ob3VycyA9IGFjdGlvbi5kYXRhLnRleHQ7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9KTtcclxuXHJcbiAgICAgICAgJC5lYWNoKGNhcmQubGFiZWxzLCBmdW5jdGlvbihpLCBsYWJlbCkge1xyXG4gICAgICAgICAgICBpZihsYWJlbC5uYW1lLnN1YnN0cmluZygwLCAzKSAhPT0gJ1NwcicpIHtcclxuICAgICAgICAgICAgICAgIGlmKGlzTmFOKHNwcmludHNbbGFiZWwubmFtZV0pKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgc3ByaW50c1tsYWJlbC5uYW1lXSA9IDA7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICBzcHJpbnRzW2xhYmVsLm5hbWVdICs9IHBhcnNlSW50KGNhcmQuaG91cnMpO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfSk7XHJcbiAgICB9KTtcclxuXHJcbiAgICAkLmVhY2goc3ByaW50cywgZnVuY3Rpb24obmFtZSwgaG91cnMpIHtcclxuICAgICAgICAkKCcjaG91cnMtYnktY2F0ZWdvcnknKS5hcHBlbmQoJzx0cj48dGQ+JyArIG5hbWUgKyAnPC90ZD48dGQ+JyArIGhvdXJzICsgJzwvdGQ+PC90cj4nKTtcclxuICAgIH0pO1xyXG59XHJcblxyXG5mdW5jdGlvbiBieVBlcnNvbihqc29uKSB7XHJcbiAgICB2YXIgcGVyc29ucyA9IHt9O1xyXG4gICAgJC5lYWNoKGpzb24uY2FyZHMsIGZ1bmN0aW9uKGksIGNhcmQpIHtcclxuICAgICAgICAvKmlmKGNhcmQuY2xvc2VkICE9PSB0cnVlKSB7XHJcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgIH0qL1xyXG4gICAgICAgIHZhciBpZFNob3J0ID0gY2FyZC5pZFNob3J0O1xyXG5cclxuICAgICAgICAkLmVhY2goanNvbi5hY3Rpb25zLCBmdW5jdGlvbihpLCBhY3Rpb24pIHtcclxuICAgICAgICAgICAgaWYoIWFjdGlvbi5kYXRhLmNhcmQpIHtcclxuICAgICAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGlmKGFjdGlvbi5kYXRhLmNhcmQuaWRTaG9ydCA9PT0gaWRTaG9ydCkge1xyXG4gICAgICAgICAgICAgICAgaWYoYWN0aW9uLnR5cGUgIT09ICdjb21tZW50Q2FyZCcpIHtcclxuICAgICAgICAgICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIGNhcmQuaG91cnMgPSBhY3Rpb24uZGF0YS50ZXh0O1xyXG4gICAgICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfSk7XHJcblxyXG4gICAgICAgICQuZWFjaChqc29uLm1lbWJlcnMsIGZ1bmN0aW9uKGksIG1lbWJlcikge1xyXG4gICAgICAgICAgICBpZihjYXJkLmlkTWVtYmVycy5sZW5ndGggPD0gMCl7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICAgICAgfVxyXG5cclxuICAgICAgICAgICAgaWYobWVtYmVyLmlkID09IGNhcmQuaWRNZW1iZXJzWzBdKSB7XHJcbiAgICAgICAgICAgICAgICBpZihpc05hTihwZXJzb25zW21lbWJlci5mdWxsTmFtZV0pKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgcGVyc29uc1ttZW1iZXIuZnVsbE5hbWVdID0gMDtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIHBlcnNvbnNbbWVtYmVyLmZ1bGxOYW1lXSArPSBwYXJzZUludChjYXJkLmhvdXJzKTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH0pO1xyXG4gICAgfSk7XHJcblxyXG4gICAgJC5lYWNoKHBlcnNvbnMsIGZ1bmN0aW9uKG5hbWUsIGhvdXJzKSB7XHJcbiAgICAgICAgJCgnI2hvdXJzLWJ5LXBlcnNvbicpLmFwcGVuZCgnPHRyPjx0ZD4nICsgbmFtZSArICc8L3RkPjx0ZD4nICsgaG91cnMgKyAnPC90ZD48L3RyPicpO1xyXG4gICAgfSk7XHJcbn1cclxuXHJcbmxvYWQoKTtcclxuIl19
