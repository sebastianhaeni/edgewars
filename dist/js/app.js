(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
require('./stupidtable');

$('table').stupidtable();

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

    $('th:nth-child(2)').click();
}

function byTask(json) {
    $.each(json.cards, function(i, card) {
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

        if(card.hours == undefined || isNaN(parseInt(card.hours))) {
            return true;
        }

        $('#hours-by-task').append('<tr><td>' + card.name + '</td><td>' + card.hours + '</td></tr>');
    });
}

function bySprint(json) {
    var sprints = {};
    $.each(json.cards, function(i, card) {
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
                var hours = parseInt(card.hours)
                if(isNaN(hours)) {
                    return true;
                }
                sprints[label.name] += hours;
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
                var hours = parseInt(card.hours)
                if(isNaN(hours)) {
                    return true;
                }
                sprints[label.name] += hours;
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
                var hours = parseInt(card.hours)
                if(isNaN(hours)) {
                    return true;
                }
                persons[member.fullName] += hours;
            }
        });
    });

    $.each(persons, function(name, hours) {
        $('#hours-by-person').append('<tr><td>' + name + '</td><td>' + hours + '</td></tr>');
    });
}

load();

},{"./stupidtable":2}],2:[function(require,module,exports){
// Stupid jQuery table plugin.

(function($) {
  $.fn.stupidtable = function(sortFns) {
    return this.each(function() {
      var $table = $(this);
      sortFns = sortFns || {};
      sortFns = $.extend({}, $.fn.stupidtable.default_sort_fns, sortFns);
      $table.data('sortFns', sortFns);

      $table.on("click.stupidtable", "thead th", function() {
          $(this).stupidsort();
      });
    });
  };


  // Expects $("#mytable").stupidtable() to have already been called.
  // Call on a table header.
  $.fn.stupidsort = function(force_direction){
    var $this_th = $(this);
    var th_index = 0; // we'll increment this soon
    var dir = $.fn.stupidtable.dir;
    var $table = $this_th.closest("table");
    var datatype = $this_th.data("sort") || null;

    // No datatype? Nothing to do.
    if (datatype === null) {
      return;
    }

    // Account for colspans
    $this_th.parents("tr").find("th").slice(0, $(this).index()).each(function() {
      var cols = $(this).attr("colspan") || 1;
      th_index += parseInt(cols,10);
    });

    var sort_dir;
    if(arguments.length == 1){
        sort_dir = force_direction;
    }
    else{
        sort_dir = force_direction || $this_th.data("sort-default") || dir.ASC;
        if ($this_th.data("sort-dir"))
           sort_dir = $this_th.data("sort-dir") === dir.ASC ? dir.DESC : dir.ASC;
    }


    $table.trigger("beforetablesort", {column: th_index, direction: sort_dir});

    // More reliable method of forcing a redraw
    $table.css("display");

    // Run sorting asynchronously on a timout to force browser redraw after
    // `beforetablesort` callback. Also avoids locking up the browser too much.
    setTimeout(function() {
      // Gather the elements for this column
      var column = [];
      var sortFns = $table.data('sortFns');
      var sortMethod = sortFns[datatype];
      var trs = $table.children("tbody").children("tr");

      // Extract the data for the column that needs to be sorted and pair it up
      // with the TR itself into a tuple. This way sorting the values will
      // incidentally sort the trs.
      trs.each(function(index,tr) {
        var $e = $(tr).children().eq(th_index);
        var sort_val = $e.data("sort-value");

        // Store and read from the .data cache for display text only sorts
        // instead of looking through the DOM every time
        if(typeof(sort_val) === "undefined"){
          var txt = $e.text();
          $e.data('sort-value', txt);
          sort_val = txt;
        }
        column.push([sort_val, tr]);
      });

      // Sort by the data-order-by value
      column.sort(function(a, b) { return sortMethod(a[0], b[0]); });
      if (sort_dir != dir.ASC)
        column.reverse();

      // Replace the content of tbody with the sorted rows. Strangely
      // enough, .append accomplishes this for us.
      trs = $.map(column, function(kv) { return kv[1]; });
      $table.children("tbody").append(trs);

      // Reset siblings
      $table.find("th").data("sort-dir", null).removeClass("sorting-desc sorting-asc");
      $this_th.data("sort-dir", sort_dir).addClass("sorting-"+sort_dir);

      $table.trigger("aftertablesort", {column: th_index, direction: sort_dir});
      $table.css("display");
    }, 10);

    return $this_th;
  };

  // Call on a sortable td to update its value in the sort. This should be the
  // only mechanism used to update a cell's sort value. If your display value is
  // different from your sort value, use jQuery's .text() or .html() to update
  // the td contents, Assumes stupidtable has already been called for the table.
  $.fn.updateSortVal = function(new_sort_val){
  var $this_td = $(this);
    if($this_td.is('[data-sort-value]')){
      // For visual consistency with the .data cache
      $this_td.attr('data-sort-value', new_sort_val);
    }
    $this_td.data("sort-value", new_sort_val);
    return $this_td;
  };

  // ------------------------------------------------------------------
  // Default settings
  // ------------------------------------------------------------------
  $.fn.stupidtable.dir = {ASC: "asc", DESC: "desc"};
  $.fn.stupidtable.default_sort_fns = {
    "int": function(a, b) {
      return parseInt(a, 10) - parseInt(b, 10);
    },
    "float": function(a, b) {
      return parseFloat(a) - parseFloat(b);
    },
    "string": function(a, b) {
      return a.localeCompare(b);
    },
    "string-ins": function(a, b) {
      a = a.toLocaleLowerCase();
      b = b.toLocaleLowerCase();
      return a.localeCompare(b);
    }
  };
})(jQuery);

},{}]},{},[1])
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIm5vZGVfbW9kdWxlcy9icm93c2VyaWZ5L25vZGVfbW9kdWxlcy9icm93c2VyLXBhY2svX3ByZWx1ZGUuanMiLCJhcHAvc2NyaXB0cy9hcHAuanMiLCJhcHAvc2NyaXB0cy9zdHVwaWR0YWJsZS5qcyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTtBQ0FBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FDbktBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBIiwiZmlsZSI6ImdlbmVyYXRlZC5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzQ29udGVudCI6WyIoZnVuY3Rpb24gZSh0LG4scil7ZnVuY3Rpb24gcyhvLHUpe2lmKCFuW29dKXtpZighdFtvXSl7dmFyIGE9dHlwZW9mIHJlcXVpcmU9PVwiZnVuY3Rpb25cIiYmcmVxdWlyZTtpZighdSYmYSlyZXR1cm4gYShvLCEwKTtpZihpKXJldHVybiBpKG8sITApO3ZhciBmPW5ldyBFcnJvcihcIkNhbm5vdCBmaW5kIG1vZHVsZSAnXCIrbytcIidcIik7dGhyb3cgZi5jb2RlPVwiTU9EVUxFX05PVF9GT1VORFwiLGZ9dmFyIGw9bltvXT17ZXhwb3J0czp7fX07dFtvXVswXS5jYWxsKGwuZXhwb3J0cyxmdW5jdGlvbihlKXt2YXIgbj10W29dWzFdW2VdO3JldHVybiBzKG4/bjplKX0sbCxsLmV4cG9ydHMsZSx0LG4scil9cmV0dXJuIG5bb10uZXhwb3J0c312YXIgaT10eXBlb2YgcmVxdWlyZT09XCJmdW5jdGlvblwiJiZyZXF1aXJlO2Zvcih2YXIgbz0wO288ci5sZW5ndGg7bysrKXMocltvXSk7cmV0dXJuIHN9KSIsInJlcXVpcmUoJy4vc3R1cGlkdGFibGUnKTtcclxuXHJcbiQoJ3RhYmxlJykuc3R1cGlkdGFibGUoKTtcclxuXHJcbmZ1bmN0aW9uIGxvYWQoKSB7XHJcbiAgICAkLmFqYXgoe1xyXG4gICAgICAgIHVybDogJy9kYXRhL2RhdGEuanNvbicsXHJcbiAgICAgICAgc3VjY2VzczogZnVuY3Rpb24ocmVzcG9uc2Upe1xyXG4gICAgICAgICAgICBwcm9jZXNzKHJlc3BvbnNlKTtcclxuICAgICAgICB9XHJcbiAgICB9KVxyXG59XHJcblxyXG5mdW5jdGlvbiBwcm9jZXNzKGpzb24pIHtcclxuICAgIGJ5VGFzayhqc29uKTtcclxuICAgIGJ5U3ByaW50KGpzb24pO1xyXG4gICAgYnlDYXRlZ29yeShqc29uKTtcclxuICAgIGJ5UGVyc29uKGpzb24pO1xyXG5cclxuICAgICQoJ3RoOm50aC1jaGlsZCgyKScpLmNsaWNrKCk7XHJcbn1cclxuXHJcbmZ1bmN0aW9uIGJ5VGFzayhqc29uKSB7XHJcbiAgICAkLmVhY2goanNvbi5jYXJkcywgZnVuY3Rpb24oaSwgY2FyZCkge1xyXG4gICAgICAgIHZhciBpZFNob3J0ID0gY2FyZC5pZFNob3J0O1xyXG5cclxuICAgICAgICAkLmVhY2goanNvbi5hY3Rpb25zLCBmdW5jdGlvbihpLCBhY3Rpb24pIHtcclxuICAgICAgICAgICAgaWYoIWFjdGlvbi5kYXRhLmNhcmQpIHtcclxuICAgICAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGlmKGFjdGlvbi5kYXRhLmNhcmQuaWRTaG9ydCA9PT0gaWRTaG9ydCkge1xyXG4gICAgICAgICAgICAgICAgaWYoYWN0aW9uLnR5cGUgIT09ICdjb21tZW50Q2FyZCcpIHtcclxuICAgICAgICAgICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIGNhcmQuaG91cnMgPSBhY3Rpb24uZGF0YS50ZXh0O1xyXG4gICAgICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfSk7XHJcblxyXG4gICAgICAgIGlmKGNhcmQuaG91cnMgPT0gdW5kZWZpbmVkIHx8IGlzTmFOKHBhcnNlSW50KGNhcmQuaG91cnMpKSkge1xyXG4gICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICB9XHJcblxyXG4gICAgICAgICQoJyNob3Vycy1ieS10YXNrJykuYXBwZW5kKCc8dHI+PHRkPicgKyBjYXJkLm5hbWUgKyAnPC90ZD48dGQ+JyArIGNhcmQuaG91cnMgKyAnPC90ZD48L3RyPicpO1xyXG4gICAgfSk7XHJcbn1cclxuXHJcbmZ1bmN0aW9uIGJ5U3ByaW50KGpzb24pIHtcclxuICAgIHZhciBzcHJpbnRzID0ge307XHJcbiAgICAkLmVhY2goanNvbi5jYXJkcywgZnVuY3Rpb24oaSwgY2FyZCkge1xyXG4gICAgICAgIHZhciBpZFNob3J0ID0gY2FyZC5pZFNob3J0O1xyXG5cclxuICAgICAgICAkLmVhY2goanNvbi5hY3Rpb25zLCBmdW5jdGlvbihpLCBhY3Rpb24pIHtcclxuICAgICAgICAgICAgaWYoIWFjdGlvbi5kYXRhLmNhcmQpIHtcclxuICAgICAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGlmKGFjdGlvbi5kYXRhLmNhcmQuaWRTaG9ydCA9PT0gaWRTaG9ydCkge1xyXG4gICAgICAgICAgICAgICAgaWYoYWN0aW9uLnR5cGUgIT09ICdjb21tZW50Q2FyZCcpIHtcclxuICAgICAgICAgICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIGNhcmQuaG91cnMgPSBhY3Rpb24uZGF0YS50ZXh0O1xyXG4gICAgICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfSk7XHJcblxyXG4gICAgICAgICQuZWFjaChjYXJkLmxhYmVscywgZnVuY3Rpb24oaSwgbGFiZWwpIHtcclxuICAgICAgICAgICAgaWYobGFiZWwubmFtZS5zdWJzdHJpbmcoMCwgMykgPT09ICdTcHInKSB7XHJcbiAgICAgICAgICAgICAgICBpZihpc05hTihzcHJpbnRzW2xhYmVsLm5hbWVdKSkge1xyXG4gICAgICAgICAgICAgICAgICAgIHNwcmludHNbbGFiZWwubmFtZV0gPSAwO1xyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgdmFyIGhvdXJzID0gcGFyc2VJbnQoY2FyZC5ob3VycylcclxuICAgICAgICAgICAgICAgIGlmKGlzTmFOKGhvdXJzKSkge1xyXG4gICAgICAgICAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgc3ByaW50c1tsYWJlbC5uYW1lXSArPSBob3VycztcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH0pO1xyXG4gICAgfSk7XHJcblxyXG4gICAgJC5lYWNoKHNwcmludHMsIGZ1bmN0aW9uKG5hbWUsIGhvdXJzKSB7XHJcbiAgICAgICAgJCgnI2hvdXJzLWJ5LXNwcmludCcpLmFwcGVuZCgnPHRyPjx0ZD4nICsgbmFtZSArICc8L3RkPjx0ZD4nICsgaG91cnMgKyAnPC90ZD48L3RyPicpO1xyXG4gICAgfSk7XHJcbn1cclxuXHJcbmZ1bmN0aW9uIGJ5Q2F0ZWdvcnkoanNvbikge1xyXG4gICAgdmFyIHNwcmludHMgPSB7fTtcclxuICAgICQuZWFjaChqc29uLmNhcmRzLCBmdW5jdGlvbihpLCBjYXJkKSB7XHJcbiAgICAgICAgdmFyIGlkU2hvcnQgPSBjYXJkLmlkU2hvcnQ7XHJcblxyXG4gICAgICAgICQuZWFjaChqc29uLmFjdGlvbnMsIGZ1bmN0aW9uKGksIGFjdGlvbikge1xyXG4gICAgICAgICAgICBpZighYWN0aW9uLmRhdGEuY2FyZCkge1xyXG4gICAgICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgaWYoYWN0aW9uLmRhdGEuY2FyZC5pZFNob3J0ID09PSBpZFNob3J0KSB7XHJcbiAgICAgICAgICAgICAgICBpZihhY3Rpb24udHlwZSAhPT0gJ2NvbW1lbnRDYXJkJykge1xyXG4gICAgICAgICAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgY2FyZC5ob3VycyA9IGFjdGlvbi5kYXRhLnRleHQ7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9KTtcclxuXHJcbiAgICAgICAgJC5lYWNoKGNhcmQubGFiZWxzLCBmdW5jdGlvbihpLCBsYWJlbCkge1xyXG4gICAgICAgICAgICBpZihsYWJlbC5uYW1lLnN1YnN0cmluZygwLCAzKSAhPT0gJ1NwcicpIHtcclxuICAgICAgICAgICAgICAgIGlmKGlzTmFOKHNwcmludHNbbGFiZWwubmFtZV0pKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgc3ByaW50c1tsYWJlbC5uYW1lXSA9IDA7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICB2YXIgaG91cnMgPSBwYXJzZUludChjYXJkLmhvdXJzKVxyXG4gICAgICAgICAgICAgICAgaWYoaXNOYU4oaG91cnMpKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICBzcHJpbnRzW2xhYmVsLm5hbWVdICs9IGhvdXJzO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfSk7XHJcbiAgICB9KTtcclxuXHJcbiAgICAkLmVhY2goc3ByaW50cywgZnVuY3Rpb24obmFtZSwgaG91cnMpIHtcclxuICAgICAgICAkKCcjaG91cnMtYnktY2F0ZWdvcnknKS5hcHBlbmQoJzx0cj48dGQ+JyArIG5hbWUgKyAnPC90ZD48dGQ+JyArIGhvdXJzICsgJzwvdGQ+PC90cj4nKTtcclxuICAgIH0pO1xyXG59XHJcblxyXG5mdW5jdGlvbiBieVBlcnNvbihqc29uKSB7XHJcbiAgICB2YXIgcGVyc29ucyA9IHt9O1xyXG4gICAgJC5lYWNoKGpzb24uY2FyZHMsIGZ1bmN0aW9uKGksIGNhcmQpIHtcclxuICAgICAgICB2YXIgaWRTaG9ydCA9IGNhcmQuaWRTaG9ydDtcclxuXHJcbiAgICAgICAgJC5lYWNoKGpzb24uYWN0aW9ucywgZnVuY3Rpb24oaSwgYWN0aW9uKSB7XHJcbiAgICAgICAgICAgIGlmKCFhY3Rpb24uZGF0YS5jYXJkKSB7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBpZihhY3Rpb24uZGF0YS5jYXJkLmlkU2hvcnQgPT09IGlkU2hvcnQpIHtcclxuICAgICAgICAgICAgICAgIGlmKGFjdGlvbi50eXBlICE9PSAnY29tbWVudENhcmQnKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICBjYXJkLmhvdXJzID0gYWN0aW9uLmRhdGEudGV4dDtcclxuICAgICAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH0pO1xyXG5cclxuICAgICAgICAkLmVhY2goanNvbi5tZW1iZXJzLCBmdW5jdGlvbihpLCBtZW1iZXIpIHtcclxuICAgICAgICAgICAgaWYoY2FyZC5pZE1lbWJlcnMubGVuZ3RoIDw9IDApe1xyXG4gICAgICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgICAgIH1cclxuXHJcbiAgICAgICAgICAgIGlmKG1lbWJlci5pZCA9PSBjYXJkLmlkTWVtYmVyc1swXSkge1xyXG4gICAgICAgICAgICAgICAgaWYoaXNOYU4ocGVyc29uc1ttZW1iZXIuZnVsbE5hbWVdKSkge1xyXG4gICAgICAgICAgICAgICAgICAgIHBlcnNvbnNbbWVtYmVyLmZ1bGxOYW1lXSA9IDA7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICB2YXIgaG91cnMgPSBwYXJzZUludChjYXJkLmhvdXJzKVxyXG4gICAgICAgICAgICAgICAgaWYoaXNOYU4oaG91cnMpKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICBwZXJzb25zW21lbWJlci5mdWxsTmFtZV0gKz0gaG91cnM7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9KTtcclxuICAgIH0pO1xyXG5cclxuICAgICQuZWFjaChwZXJzb25zLCBmdW5jdGlvbihuYW1lLCBob3Vycykge1xyXG4gICAgICAgICQoJyNob3Vycy1ieS1wZXJzb24nKS5hcHBlbmQoJzx0cj48dGQ+JyArIG5hbWUgKyAnPC90ZD48dGQ+JyArIGhvdXJzICsgJzwvdGQ+PC90cj4nKTtcclxuICAgIH0pO1xyXG59XHJcblxyXG5sb2FkKCk7XHJcbiIsIi8vIFN0dXBpZCBqUXVlcnkgdGFibGUgcGx1Z2luLlxuXG4oZnVuY3Rpb24oJCkge1xuICAkLmZuLnN0dXBpZHRhYmxlID0gZnVuY3Rpb24oc29ydEZucykge1xuICAgIHJldHVybiB0aGlzLmVhY2goZnVuY3Rpb24oKSB7XG4gICAgICB2YXIgJHRhYmxlID0gJCh0aGlzKTtcbiAgICAgIHNvcnRGbnMgPSBzb3J0Rm5zIHx8IHt9O1xuICAgICAgc29ydEZucyA9ICQuZXh0ZW5kKHt9LCAkLmZuLnN0dXBpZHRhYmxlLmRlZmF1bHRfc29ydF9mbnMsIHNvcnRGbnMpO1xuICAgICAgJHRhYmxlLmRhdGEoJ3NvcnRGbnMnLCBzb3J0Rm5zKTtcblxuICAgICAgJHRhYmxlLm9uKFwiY2xpY2suc3R1cGlkdGFibGVcIiwgXCJ0aGVhZCB0aFwiLCBmdW5jdGlvbigpIHtcbiAgICAgICAgICAkKHRoaXMpLnN0dXBpZHNvcnQoKTtcbiAgICAgIH0pO1xuICAgIH0pO1xuICB9O1xuXG5cbiAgLy8gRXhwZWN0cyAkKFwiI215dGFibGVcIikuc3R1cGlkdGFibGUoKSB0byBoYXZlIGFscmVhZHkgYmVlbiBjYWxsZWQuXG4gIC8vIENhbGwgb24gYSB0YWJsZSBoZWFkZXIuXG4gICQuZm4uc3R1cGlkc29ydCA9IGZ1bmN0aW9uKGZvcmNlX2RpcmVjdGlvbil7XG4gICAgdmFyICR0aGlzX3RoID0gJCh0aGlzKTtcbiAgICB2YXIgdGhfaW5kZXggPSAwOyAvLyB3ZSdsbCBpbmNyZW1lbnQgdGhpcyBzb29uXG4gICAgdmFyIGRpciA9ICQuZm4uc3R1cGlkdGFibGUuZGlyO1xuICAgIHZhciAkdGFibGUgPSAkdGhpc190aC5jbG9zZXN0KFwidGFibGVcIik7XG4gICAgdmFyIGRhdGF0eXBlID0gJHRoaXNfdGguZGF0YShcInNvcnRcIikgfHwgbnVsbDtcblxuICAgIC8vIE5vIGRhdGF0eXBlPyBOb3RoaW5nIHRvIGRvLlxuICAgIGlmIChkYXRhdHlwZSA9PT0gbnVsbCkge1xuICAgICAgcmV0dXJuO1xuICAgIH1cblxuICAgIC8vIEFjY291bnQgZm9yIGNvbHNwYW5zXG4gICAgJHRoaXNfdGgucGFyZW50cyhcInRyXCIpLmZpbmQoXCJ0aFwiKS5zbGljZSgwLCAkKHRoaXMpLmluZGV4KCkpLmVhY2goZnVuY3Rpb24oKSB7XG4gICAgICB2YXIgY29scyA9ICQodGhpcykuYXR0cihcImNvbHNwYW5cIikgfHwgMTtcbiAgICAgIHRoX2luZGV4ICs9IHBhcnNlSW50KGNvbHMsMTApO1xuICAgIH0pO1xuXG4gICAgdmFyIHNvcnRfZGlyO1xuICAgIGlmKGFyZ3VtZW50cy5sZW5ndGggPT0gMSl7XG4gICAgICAgIHNvcnRfZGlyID0gZm9yY2VfZGlyZWN0aW9uO1xuICAgIH1cbiAgICBlbHNle1xuICAgICAgICBzb3J0X2RpciA9IGZvcmNlX2RpcmVjdGlvbiB8fCAkdGhpc190aC5kYXRhKFwic29ydC1kZWZhdWx0XCIpIHx8IGRpci5BU0M7XG4gICAgICAgIGlmICgkdGhpc190aC5kYXRhKFwic29ydC1kaXJcIikpXG4gICAgICAgICAgIHNvcnRfZGlyID0gJHRoaXNfdGguZGF0YShcInNvcnQtZGlyXCIpID09PSBkaXIuQVNDID8gZGlyLkRFU0MgOiBkaXIuQVNDO1xuICAgIH1cblxuXG4gICAgJHRhYmxlLnRyaWdnZXIoXCJiZWZvcmV0YWJsZXNvcnRcIiwge2NvbHVtbjogdGhfaW5kZXgsIGRpcmVjdGlvbjogc29ydF9kaXJ9KTtcblxuICAgIC8vIE1vcmUgcmVsaWFibGUgbWV0aG9kIG9mIGZvcmNpbmcgYSByZWRyYXdcbiAgICAkdGFibGUuY3NzKFwiZGlzcGxheVwiKTtcblxuICAgIC8vIFJ1biBzb3J0aW5nIGFzeW5jaHJvbm91c2x5IG9uIGEgdGltb3V0IHRvIGZvcmNlIGJyb3dzZXIgcmVkcmF3IGFmdGVyXG4gICAgLy8gYGJlZm9yZXRhYmxlc29ydGAgY2FsbGJhY2suIEFsc28gYXZvaWRzIGxvY2tpbmcgdXAgdGhlIGJyb3dzZXIgdG9vIG11Y2guXG4gICAgc2V0VGltZW91dChmdW5jdGlvbigpIHtcbiAgICAgIC8vIEdhdGhlciB0aGUgZWxlbWVudHMgZm9yIHRoaXMgY29sdW1uXG4gICAgICB2YXIgY29sdW1uID0gW107XG4gICAgICB2YXIgc29ydEZucyA9ICR0YWJsZS5kYXRhKCdzb3J0Rm5zJyk7XG4gICAgICB2YXIgc29ydE1ldGhvZCA9IHNvcnRGbnNbZGF0YXR5cGVdO1xuICAgICAgdmFyIHRycyA9ICR0YWJsZS5jaGlsZHJlbihcInRib2R5XCIpLmNoaWxkcmVuKFwidHJcIik7XG5cbiAgICAgIC8vIEV4dHJhY3QgdGhlIGRhdGEgZm9yIHRoZSBjb2x1bW4gdGhhdCBuZWVkcyB0byBiZSBzb3J0ZWQgYW5kIHBhaXIgaXQgdXBcbiAgICAgIC8vIHdpdGggdGhlIFRSIGl0c2VsZiBpbnRvIGEgdHVwbGUuIFRoaXMgd2F5IHNvcnRpbmcgdGhlIHZhbHVlcyB3aWxsXG4gICAgICAvLyBpbmNpZGVudGFsbHkgc29ydCB0aGUgdHJzLlxuICAgICAgdHJzLmVhY2goZnVuY3Rpb24oaW5kZXgsdHIpIHtcbiAgICAgICAgdmFyICRlID0gJCh0cikuY2hpbGRyZW4oKS5lcSh0aF9pbmRleCk7XG4gICAgICAgIHZhciBzb3J0X3ZhbCA9ICRlLmRhdGEoXCJzb3J0LXZhbHVlXCIpO1xuXG4gICAgICAgIC8vIFN0b3JlIGFuZCByZWFkIGZyb20gdGhlIC5kYXRhIGNhY2hlIGZvciBkaXNwbGF5IHRleHQgb25seSBzb3J0c1xuICAgICAgICAvLyBpbnN0ZWFkIG9mIGxvb2tpbmcgdGhyb3VnaCB0aGUgRE9NIGV2ZXJ5IHRpbWVcbiAgICAgICAgaWYodHlwZW9mKHNvcnRfdmFsKSA9PT0gXCJ1bmRlZmluZWRcIil7XG4gICAgICAgICAgdmFyIHR4dCA9ICRlLnRleHQoKTtcbiAgICAgICAgICAkZS5kYXRhKCdzb3J0LXZhbHVlJywgdHh0KTtcbiAgICAgICAgICBzb3J0X3ZhbCA9IHR4dDtcbiAgICAgICAgfVxuICAgICAgICBjb2x1bW4ucHVzaChbc29ydF92YWwsIHRyXSk7XG4gICAgICB9KTtcblxuICAgICAgLy8gU29ydCBieSB0aGUgZGF0YS1vcmRlci1ieSB2YWx1ZVxuICAgICAgY29sdW1uLnNvcnQoZnVuY3Rpb24oYSwgYikgeyByZXR1cm4gc29ydE1ldGhvZChhWzBdLCBiWzBdKTsgfSk7XG4gICAgICBpZiAoc29ydF9kaXIgIT0gZGlyLkFTQylcbiAgICAgICAgY29sdW1uLnJldmVyc2UoKTtcblxuICAgICAgLy8gUmVwbGFjZSB0aGUgY29udGVudCBvZiB0Ym9keSB3aXRoIHRoZSBzb3J0ZWQgcm93cy4gU3RyYW5nZWx5XG4gICAgICAvLyBlbm91Z2gsIC5hcHBlbmQgYWNjb21wbGlzaGVzIHRoaXMgZm9yIHVzLlxuICAgICAgdHJzID0gJC5tYXAoY29sdW1uLCBmdW5jdGlvbihrdikgeyByZXR1cm4ga3ZbMV07IH0pO1xuICAgICAgJHRhYmxlLmNoaWxkcmVuKFwidGJvZHlcIikuYXBwZW5kKHRycyk7XG5cbiAgICAgIC8vIFJlc2V0IHNpYmxpbmdzXG4gICAgICAkdGFibGUuZmluZChcInRoXCIpLmRhdGEoXCJzb3J0LWRpclwiLCBudWxsKS5yZW1vdmVDbGFzcyhcInNvcnRpbmctZGVzYyBzb3J0aW5nLWFzY1wiKTtcbiAgICAgICR0aGlzX3RoLmRhdGEoXCJzb3J0LWRpclwiLCBzb3J0X2RpcikuYWRkQ2xhc3MoXCJzb3J0aW5nLVwiK3NvcnRfZGlyKTtcblxuICAgICAgJHRhYmxlLnRyaWdnZXIoXCJhZnRlcnRhYmxlc29ydFwiLCB7Y29sdW1uOiB0aF9pbmRleCwgZGlyZWN0aW9uOiBzb3J0X2Rpcn0pO1xuICAgICAgJHRhYmxlLmNzcyhcImRpc3BsYXlcIik7XG4gICAgfSwgMTApO1xuXG4gICAgcmV0dXJuICR0aGlzX3RoO1xuICB9O1xuXG4gIC8vIENhbGwgb24gYSBzb3J0YWJsZSB0ZCB0byB1cGRhdGUgaXRzIHZhbHVlIGluIHRoZSBzb3J0LiBUaGlzIHNob3VsZCBiZSB0aGVcbiAgLy8gb25seSBtZWNoYW5pc20gdXNlZCB0byB1cGRhdGUgYSBjZWxsJ3Mgc29ydCB2YWx1ZS4gSWYgeW91ciBkaXNwbGF5IHZhbHVlIGlzXG4gIC8vIGRpZmZlcmVudCBmcm9tIHlvdXIgc29ydCB2YWx1ZSwgdXNlIGpRdWVyeSdzIC50ZXh0KCkgb3IgLmh0bWwoKSB0byB1cGRhdGVcbiAgLy8gdGhlIHRkIGNvbnRlbnRzLCBBc3N1bWVzIHN0dXBpZHRhYmxlIGhhcyBhbHJlYWR5IGJlZW4gY2FsbGVkIGZvciB0aGUgdGFibGUuXG4gICQuZm4udXBkYXRlU29ydFZhbCA9IGZ1bmN0aW9uKG5ld19zb3J0X3ZhbCl7XG4gIHZhciAkdGhpc190ZCA9ICQodGhpcyk7XG4gICAgaWYoJHRoaXNfdGQuaXMoJ1tkYXRhLXNvcnQtdmFsdWVdJykpe1xuICAgICAgLy8gRm9yIHZpc3VhbCBjb25zaXN0ZW5jeSB3aXRoIHRoZSAuZGF0YSBjYWNoZVxuICAgICAgJHRoaXNfdGQuYXR0cignZGF0YS1zb3J0LXZhbHVlJywgbmV3X3NvcnRfdmFsKTtcbiAgICB9XG4gICAgJHRoaXNfdGQuZGF0YShcInNvcnQtdmFsdWVcIiwgbmV3X3NvcnRfdmFsKTtcbiAgICByZXR1cm4gJHRoaXNfdGQ7XG4gIH07XG5cbiAgLy8gLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tXG4gIC8vIERlZmF1bHQgc2V0dGluZ3NcbiAgLy8gLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tXG4gICQuZm4uc3R1cGlkdGFibGUuZGlyID0ge0FTQzogXCJhc2NcIiwgREVTQzogXCJkZXNjXCJ9O1xuICAkLmZuLnN0dXBpZHRhYmxlLmRlZmF1bHRfc29ydF9mbnMgPSB7XG4gICAgXCJpbnRcIjogZnVuY3Rpb24oYSwgYikge1xuICAgICAgcmV0dXJuIHBhcnNlSW50KGEsIDEwKSAtIHBhcnNlSW50KGIsIDEwKTtcbiAgICB9LFxuICAgIFwiZmxvYXRcIjogZnVuY3Rpb24oYSwgYikge1xuICAgICAgcmV0dXJuIHBhcnNlRmxvYXQoYSkgLSBwYXJzZUZsb2F0KGIpO1xuICAgIH0sXG4gICAgXCJzdHJpbmdcIjogZnVuY3Rpb24oYSwgYikge1xuICAgICAgcmV0dXJuIGEubG9jYWxlQ29tcGFyZShiKTtcbiAgICB9LFxuICAgIFwic3RyaW5nLWluc1wiOiBmdW5jdGlvbihhLCBiKSB7XG4gICAgICBhID0gYS50b0xvY2FsZUxvd2VyQ2FzZSgpO1xuICAgICAgYiA9IGIudG9Mb2NhbGVMb3dlckNhc2UoKTtcbiAgICAgIHJldHVybiBhLmxvY2FsZUNvbXBhcmUoYik7XG4gICAgfVxuICB9O1xufSkoalF1ZXJ5KTtcbiJdfQ==
