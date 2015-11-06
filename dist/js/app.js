(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
require('./stupidtable');

$('table').stupidtable();

function load() {
    $.ajax({
        url: 'data/data.json',
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
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIm5vZGVfbW9kdWxlcy9icm93c2VyaWZ5L25vZGVfbW9kdWxlcy9icm93c2VyLXBhY2svX3ByZWx1ZGUuanMiLCJhcHAvc2NyaXB0cy9hcHAuanMiLCJhcHAvc2NyaXB0cy9zdHVwaWR0YWJsZS5qcyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTtBQ0FBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FDbktBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBIiwiZmlsZSI6ImdlbmVyYXRlZC5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzQ29udGVudCI6WyIoZnVuY3Rpb24gZSh0LG4scil7ZnVuY3Rpb24gcyhvLHUpe2lmKCFuW29dKXtpZighdFtvXSl7dmFyIGE9dHlwZW9mIHJlcXVpcmU9PVwiZnVuY3Rpb25cIiYmcmVxdWlyZTtpZighdSYmYSlyZXR1cm4gYShvLCEwKTtpZihpKXJldHVybiBpKG8sITApO3ZhciBmPW5ldyBFcnJvcihcIkNhbm5vdCBmaW5kIG1vZHVsZSAnXCIrbytcIidcIik7dGhyb3cgZi5jb2RlPVwiTU9EVUxFX05PVF9GT1VORFwiLGZ9dmFyIGw9bltvXT17ZXhwb3J0czp7fX07dFtvXVswXS5jYWxsKGwuZXhwb3J0cyxmdW5jdGlvbihlKXt2YXIgbj10W29dWzFdW2VdO3JldHVybiBzKG4/bjplKX0sbCxsLmV4cG9ydHMsZSx0LG4scil9cmV0dXJuIG5bb10uZXhwb3J0c312YXIgaT10eXBlb2YgcmVxdWlyZT09XCJmdW5jdGlvblwiJiZyZXF1aXJlO2Zvcih2YXIgbz0wO288ci5sZW5ndGg7bysrKXMocltvXSk7cmV0dXJuIHN9KSIsInJlcXVpcmUoJy4vc3R1cGlkdGFibGUnKTtcclxuXHJcbiQoJ3RhYmxlJykuc3R1cGlkdGFibGUoKTtcclxuXHJcbmZ1bmN0aW9uIGxvYWQoKSB7XHJcbiAgICAkLmFqYXgoe1xyXG4gICAgICAgIHVybDogJ2RhdGEvZGF0YS5qc29uJyxcclxuICAgICAgICBzdWNjZXNzOiBmdW5jdGlvbihyZXNwb25zZSl7XHJcbiAgICAgICAgICAgIHByb2Nlc3MocmVzcG9uc2UpO1xyXG4gICAgICAgIH1cclxuICAgIH0pXHJcbn1cclxuXHJcbmZ1bmN0aW9uIHByb2Nlc3MoanNvbikge1xyXG4gICAgYnlUYXNrKGpzb24pO1xyXG4gICAgYnlTcHJpbnQoanNvbik7XHJcbiAgICBieUNhdGVnb3J5KGpzb24pO1xyXG4gICAgYnlQZXJzb24oanNvbik7XHJcblxyXG4gICAgJCgndGg6bnRoLWNoaWxkKDIpJykuY2xpY2soKTtcclxufVxyXG5cclxuZnVuY3Rpb24gYnlUYXNrKGpzb24pIHtcclxuICAgICQuZWFjaChqc29uLmNhcmRzLCBmdW5jdGlvbihpLCBjYXJkKSB7XHJcbiAgICAgICAgdmFyIGlkU2hvcnQgPSBjYXJkLmlkU2hvcnQ7XHJcblxyXG4gICAgICAgICQuZWFjaChqc29uLmFjdGlvbnMsIGZ1bmN0aW9uKGksIGFjdGlvbikge1xyXG4gICAgICAgICAgICBpZighYWN0aW9uLmRhdGEuY2FyZCkge1xyXG4gICAgICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgaWYoYWN0aW9uLmRhdGEuY2FyZC5pZFNob3J0ID09PSBpZFNob3J0KSB7XHJcbiAgICAgICAgICAgICAgICBpZihhY3Rpb24udHlwZSAhPT0gJ2NvbW1lbnRDYXJkJykge1xyXG4gICAgICAgICAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgY2FyZC5ob3VycyA9IGFjdGlvbi5kYXRhLnRleHQ7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9KTtcclxuXHJcbiAgICAgICAgaWYoY2FyZC5ob3VycyA9PSB1bmRlZmluZWQgfHwgaXNOYU4ocGFyc2VJbnQoY2FyZC5ob3VycykpKSB7XHJcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgIH1cclxuXHJcbiAgICAgICAgJCgnI2hvdXJzLWJ5LXRhc2snKS5hcHBlbmQoJzx0cj48dGQ+JyArIGNhcmQubmFtZSArICc8L3RkPjx0ZD4nICsgY2FyZC5ob3VycyArICc8L3RkPjwvdHI+Jyk7XHJcbiAgICB9KTtcclxufVxyXG5cclxuZnVuY3Rpb24gYnlTcHJpbnQoanNvbikge1xyXG4gICAgdmFyIHNwcmludHMgPSB7fTtcclxuICAgICQuZWFjaChqc29uLmNhcmRzLCBmdW5jdGlvbihpLCBjYXJkKSB7XHJcbiAgICAgICAgdmFyIGlkU2hvcnQgPSBjYXJkLmlkU2hvcnQ7XHJcblxyXG4gICAgICAgICQuZWFjaChqc29uLmFjdGlvbnMsIGZ1bmN0aW9uKGksIGFjdGlvbikge1xyXG4gICAgICAgICAgICBpZighYWN0aW9uLmRhdGEuY2FyZCkge1xyXG4gICAgICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgaWYoYWN0aW9uLmRhdGEuY2FyZC5pZFNob3J0ID09PSBpZFNob3J0KSB7XHJcbiAgICAgICAgICAgICAgICBpZihhY3Rpb24udHlwZSAhPT0gJ2NvbW1lbnRDYXJkJykge1xyXG4gICAgICAgICAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgY2FyZC5ob3VycyA9IGFjdGlvbi5kYXRhLnRleHQ7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9KTtcclxuXHJcbiAgICAgICAgJC5lYWNoKGNhcmQubGFiZWxzLCBmdW5jdGlvbihpLCBsYWJlbCkge1xyXG4gICAgICAgICAgICBpZihsYWJlbC5uYW1lLnN1YnN0cmluZygwLCAzKSA9PT0gJ1NwcicpIHtcclxuICAgICAgICAgICAgICAgIGlmKGlzTmFOKHNwcmludHNbbGFiZWwubmFtZV0pKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgc3ByaW50c1tsYWJlbC5uYW1lXSA9IDA7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICB2YXIgaG91cnMgPSBwYXJzZUludChjYXJkLmhvdXJzKVxyXG4gICAgICAgICAgICAgICAgaWYoaXNOYU4oaG91cnMpKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICBzcHJpbnRzW2xhYmVsLm5hbWVdICs9IGhvdXJzO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfSk7XHJcbiAgICB9KTtcclxuXHJcbiAgICAkLmVhY2goc3ByaW50cywgZnVuY3Rpb24obmFtZSwgaG91cnMpIHtcclxuICAgICAgICAkKCcjaG91cnMtYnktc3ByaW50JykuYXBwZW5kKCc8dHI+PHRkPicgKyBuYW1lICsgJzwvdGQ+PHRkPicgKyBob3VycyArICc8L3RkPjwvdHI+Jyk7XHJcbiAgICB9KTtcclxufVxyXG5cclxuZnVuY3Rpb24gYnlDYXRlZ29yeShqc29uKSB7XHJcbiAgICB2YXIgc3ByaW50cyA9IHt9O1xyXG4gICAgJC5lYWNoKGpzb24uY2FyZHMsIGZ1bmN0aW9uKGksIGNhcmQpIHtcclxuICAgICAgICB2YXIgaWRTaG9ydCA9IGNhcmQuaWRTaG9ydDtcclxuXHJcbiAgICAgICAgJC5lYWNoKGpzb24uYWN0aW9ucywgZnVuY3Rpb24oaSwgYWN0aW9uKSB7XHJcbiAgICAgICAgICAgIGlmKCFhY3Rpb24uZGF0YS5jYXJkKSB7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBpZihhY3Rpb24uZGF0YS5jYXJkLmlkU2hvcnQgPT09IGlkU2hvcnQpIHtcclxuICAgICAgICAgICAgICAgIGlmKGFjdGlvbi50eXBlICE9PSAnY29tbWVudENhcmQnKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICBjYXJkLmhvdXJzID0gYWN0aW9uLmRhdGEudGV4dDtcclxuICAgICAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH0pO1xyXG5cclxuICAgICAgICAkLmVhY2goY2FyZC5sYWJlbHMsIGZ1bmN0aW9uKGksIGxhYmVsKSB7XHJcbiAgICAgICAgICAgIGlmKGxhYmVsLm5hbWUuc3Vic3RyaW5nKDAsIDMpICE9PSAnU3ByJykge1xyXG4gICAgICAgICAgICAgICAgaWYoaXNOYU4oc3ByaW50c1tsYWJlbC5uYW1lXSkpIHtcclxuICAgICAgICAgICAgICAgICAgICBzcHJpbnRzW2xhYmVsLm5hbWVdID0gMDtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIHZhciBob3VycyA9IHBhcnNlSW50KGNhcmQuaG91cnMpXHJcbiAgICAgICAgICAgICAgICBpZihpc05hTihob3VycykpIHtcclxuICAgICAgICAgICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIHNwcmludHNbbGFiZWwubmFtZV0gKz0gaG91cnM7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9KTtcclxuICAgIH0pO1xyXG5cclxuICAgICQuZWFjaChzcHJpbnRzLCBmdW5jdGlvbihuYW1lLCBob3Vycykge1xyXG4gICAgICAgICQoJyNob3Vycy1ieS1jYXRlZ29yeScpLmFwcGVuZCgnPHRyPjx0ZD4nICsgbmFtZSArICc8L3RkPjx0ZD4nICsgaG91cnMgKyAnPC90ZD48L3RyPicpO1xyXG4gICAgfSk7XHJcbn1cclxuXHJcbmZ1bmN0aW9uIGJ5UGVyc29uKGpzb24pIHtcclxuICAgIHZhciBwZXJzb25zID0ge307XHJcbiAgICAkLmVhY2goanNvbi5jYXJkcywgZnVuY3Rpb24oaSwgY2FyZCkge1xyXG4gICAgICAgIHZhciBpZFNob3J0ID0gY2FyZC5pZFNob3J0O1xyXG5cclxuICAgICAgICAkLmVhY2goanNvbi5hY3Rpb25zLCBmdW5jdGlvbihpLCBhY3Rpb24pIHtcclxuICAgICAgICAgICAgaWYoIWFjdGlvbi5kYXRhLmNhcmQpIHtcclxuICAgICAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGlmKGFjdGlvbi5kYXRhLmNhcmQuaWRTaG9ydCA9PT0gaWRTaG9ydCkge1xyXG4gICAgICAgICAgICAgICAgaWYoYWN0aW9uLnR5cGUgIT09ICdjb21tZW50Q2FyZCcpIHtcclxuICAgICAgICAgICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIGNhcmQuaG91cnMgPSBhY3Rpb24uZGF0YS50ZXh0O1xyXG4gICAgICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfSk7XHJcblxyXG4gICAgICAgICQuZWFjaChqc29uLm1lbWJlcnMsIGZ1bmN0aW9uKGksIG1lbWJlcikge1xyXG4gICAgICAgICAgICBpZihjYXJkLmlkTWVtYmVycy5sZW5ndGggPD0gMCl7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICAgICAgfVxyXG5cclxuICAgICAgICAgICAgaWYobWVtYmVyLmlkID09IGNhcmQuaWRNZW1iZXJzWzBdKSB7XHJcbiAgICAgICAgICAgICAgICBpZihpc05hTihwZXJzb25zW21lbWJlci5mdWxsTmFtZV0pKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgcGVyc29uc1ttZW1iZXIuZnVsbE5hbWVdID0gMDtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIHZhciBob3VycyA9IHBhcnNlSW50KGNhcmQuaG91cnMpXHJcbiAgICAgICAgICAgICAgICBpZihpc05hTihob3VycykpIHtcclxuICAgICAgICAgICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIHBlcnNvbnNbbWVtYmVyLmZ1bGxOYW1lXSArPSBob3VycztcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH0pO1xyXG4gICAgfSk7XHJcblxyXG4gICAgJC5lYWNoKHBlcnNvbnMsIGZ1bmN0aW9uKG5hbWUsIGhvdXJzKSB7XHJcbiAgICAgICAgJCgnI2hvdXJzLWJ5LXBlcnNvbicpLmFwcGVuZCgnPHRyPjx0ZD4nICsgbmFtZSArICc8L3RkPjx0ZD4nICsgaG91cnMgKyAnPC90ZD48L3RyPicpO1xyXG4gICAgfSk7XHJcbn1cclxuXHJcbmxvYWQoKTtcclxuIiwiLy8gU3R1cGlkIGpRdWVyeSB0YWJsZSBwbHVnaW4uXHJcblxyXG4oZnVuY3Rpb24oJCkge1xyXG4gICQuZm4uc3R1cGlkdGFibGUgPSBmdW5jdGlvbihzb3J0Rm5zKSB7XHJcbiAgICByZXR1cm4gdGhpcy5lYWNoKGZ1bmN0aW9uKCkge1xyXG4gICAgICB2YXIgJHRhYmxlID0gJCh0aGlzKTtcclxuICAgICAgc29ydEZucyA9IHNvcnRGbnMgfHwge307XHJcbiAgICAgIHNvcnRGbnMgPSAkLmV4dGVuZCh7fSwgJC5mbi5zdHVwaWR0YWJsZS5kZWZhdWx0X3NvcnRfZm5zLCBzb3J0Rm5zKTtcclxuICAgICAgJHRhYmxlLmRhdGEoJ3NvcnRGbnMnLCBzb3J0Rm5zKTtcclxuXHJcbiAgICAgICR0YWJsZS5vbihcImNsaWNrLnN0dXBpZHRhYmxlXCIsIFwidGhlYWQgdGhcIiwgZnVuY3Rpb24oKSB7XHJcbiAgICAgICAgICAkKHRoaXMpLnN0dXBpZHNvcnQoKTtcclxuICAgICAgfSk7XHJcbiAgICB9KTtcclxuICB9O1xyXG5cclxuXHJcbiAgLy8gRXhwZWN0cyAkKFwiI215dGFibGVcIikuc3R1cGlkdGFibGUoKSB0byBoYXZlIGFscmVhZHkgYmVlbiBjYWxsZWQuXHJcbiAgLy8gQ2FsbCBvbiBhIHRhYmxlIGhlYWRlci5cclxuICAkLmZuLnN0dXBpZHNvcnQgPSBmdW5jdGlvbihmb3JjZV9kaXJlY3Rpb24pe1xyXG4gICAgdmFyICR0aGlzX3RoID0gJCh0aGlzKTtcclxuICAgIHZhciB0aF9pbmRleCA9IDA7IC8vIHdlJ2xsIGluY3JlbWVudCB0aGlzIHNvb25cclxuICAgIHZhciBkaXIgPSAkLmZuLnN0dXBpZHRhYmxlLmRpcjtcclxuICAgIHZhciAkdGFibGUgPSAkdGhpc190aC5jbG9zZXN0KFwidGFibGVcIik7XHJcbiAgICB2YXIgZGF0YXR5cGUgPSAkdGhpc190aC5kYXRhKFwic29ydFwiKSB8fCBudWxsO1xyXG5cclxuICAgIC8vIE5vIGRhdGF0eXBlPyBOb3RoaW5nIHRvIGRvLlxyXG4gICAgaWYgKGRhdGF0eXBlID09PSBudWxsKSB7XHJcbiAgICAgIHJldHVybjtcclxuICAgIH1cclxuXHJcbiAgICAvLyBBY2NvdW50IGZvciBjb2xzcGFuc1xyXG4gICAgJHRoaXNfdGgucGFyZW50cyhcInRyXCIpLmZpbmQoXCJ0aFwiKS5zbGljZSgwLCAkKHRoaXMpLmluZGV4KCkpLmVhY2goZnVuY3Rpb24oKSB7XHJcbiAgICAgIHZhciBjb2xzID0gJCh0aGlzKS5hdHRyKFwiY29sc3BhblwiKSB8fCAxO1xyXG4gICAgICB0aF9pbmRleCArPSBwYXJzZUludChjb2xzLDEwKTtcclxuICAgIH0pO1xyXG5cclxuICAgIHZhciBzb3J0X2RpcjtcclxuICAgIGlmKGFyZ3VtZW50cy5sZW5ndGggPT0gMSl7XHJcbiAgICAgICAgc29ydF9kaXIgPSBmb3JjZV9kaXJlY3Rpb247XHJcbiAgICB9XHJcbiAgICBlbHNle1xyXG4gICAgICAgIHNvcnRfZGlyID0gZm9yY2VfZGlyZWN0aW9uIHx8ICR0aGlzX3RoLmRhdGEoXCJzb3J0LWRlZmF1bHRcIikgfHwgZGlyLkFTQztcclxuICAgICAgICBpZiAoJHRoaXNfdGguZGF0YShcInNvcnQtZGlyXCIpKVxyXG4gICAgICAgICAgIHNvcnRfZGlyID0gJHRoaXNfdGguZGF0YShcInNvcnQtZGlyXCIpID09PSBkaXIuQVNDID8gZGlyLkRFU0MgOiBkaXIuQVNDO1xyXG4gICAgfVxyXG5cclxuXHJcbiAgICAkdGFibGUudHJpZ2dlcihcImJlZm9yZXRhYmxlc29ydFwiLCB7Y29sdW1uOiB0aF9pbmRleCwgZGlyZWN0aW9uOiBzb3J0X2Rpcn0pO1xyXG5cclxuICAgIC8vIE1vcmUgcmVsaWFibGUgbWV0aG9kIG9mIGZvcmNpbmcgYSByZWRyYXdcclxuICAgICR0YWJsZS5jc3MoXCJkaXNwbGF5XCIpO1xyXG5cclxuICAgIC8vIFJ1biBzb3J0aW5nIGFzeW5jaHJvbm91c2x5IG9uIGEgdGltb3V0IHRvIGZvcmNlIGJyb3dzZXIgcmVkcmF3IGFmdGVyXHJcbiAgICAvLyBgYmVmb3JldGFibGVzb3J0YCBjYWxsYmFjay4gQWxzbyBhdm9pZHMgbG9ja2luZyB1cCB0aGUgYnJvd3NlciB0b28gbXVjaC5cclxuICAgIHNldFRpbWVvdXQoZnVuY3Rpb24oKSB7XHJcbiAgICAgIC8vIEdhdGhlciB0aGUgZWxlbWVudHMgZm9yIHRoaXMgY29sdW1uXHJcbiAgICAgIHZhciBjb2x1bW4gPSBbXTtcclxuICAgICAgdmFyIHNvcnRGbnMgPSAkdGFibGUuZGF0YSgnc29ydEZucycpO1xyXG4gICAgICB2YXIgc29ydE1ldGhvZCA9IHNvcnRGbnNbZGF0YXR5cGVdO1xyXG4gICAgICB2YXIgdHJzID0gJHRhYmxlLmNoaWxkcmVuKFwidGJvZHlcIikuY2hpbGRyZW4oXCJ0clwiKTtcclxuXHJcbiAgICAgIC8vIEV4dHJhY3QgdGhlIGRhdGEgZm9yIHRoZSBjb2x1bW4gdGhhdCBuZWVkcyB0byBiZSBzb3J0ZWQgYW5kIHBhaXIgaXQgdXBcclxuICAgICAgLy8gd2l0aCB0aGUgVFIgaXRzZWxmIGludG8gYSB0dXBsZS4gVGhpcyB3YXkgc29ydGluZyB0aGUgdmFsdWVzIHdpbGxcclxuICAgICAgLy8gaW5jaWRlbnRhbGx5IHNvcnQgdGhlIHRycy5cclxuICAgICAgdHJzLmVhY2goZnVuY3Rpb24oaW5kZXgsdHIpIHtcclxuICAgICAgICB2YXIgJGUgPSAkKHRyKS5jaGlsZHJlbigpLmVxKHRoX2luZGV4KTtcclxuICAgICAgICB2YXIgc29ydF92YWwgPSAkZS5kYXRhKFwic29ydC12YWx1ZVwiKTtcclxuXHJcbiAgICAgICAgLy8gU3RvcmUgYW5kIHJlYWQgZnJvbSB0aGUgLmRhdGEgY2FjaGUgZm9yIGRpc3BsYXkgdGV4dCBvbmx5IHNvcnRzXHJcbiAgICAgICAgLy8gaW5zdGVhZCBvZiBsb29raW5nIHRocm91Z2ggdGhlIERPTSBldmVyeSB0aW1lXHJcbiAgICAgICAgaWYodHlwZW9mKHNvcnRfdmFsKSA9PT0gXCJ1bmRlZmluZWRcIil7XHJcbiAgICAgICAgICB2YXIgdHh0ID0gJGUudGV4dCgpO1xyXG4gICAgICAgICAgJGUuZGF0YSgnc29ydC12YWx1ZScsIHR4dCk7XHJcbiAgICAgICAgICBzb3J0X3ZhbCA9IHR4dDtcclxuICAgICAgICB9XHJcbiAgICAgICAgY29sdW1uLnB1c2goW3NvcnRfdmFsLCB0cl0pO1xyXG4gICAgICB9KTtcclxuXHJcbiAgICAgIC8vIFNvcnQgYnkgdGhlIGRhdGEtb3JkZXItYnkgdmFsdWVcclxuICAgICAgY29sdW1uLnNvcnQoZnVuY3Rpb24oYSwgYikgeyByZXR1cm4gc29ydE1ldGhvZChhWzBdLCBiWzBdKTsgfSk7XHJcbiAgICAgIGlmIChzb3J0X2RpciAhPSBkaXIuQVNDKVxyXG4gICAgICAgIGNvbHVtbi5yZXZlcnNlKCk7XHJcblxyXG4gICAgICAvLyBSZXBsYWNlIHRoZSBjb250ZW50IG9mIHRib2R5IHdpdGggdGhlIHNvcnRlZCByb3dzLiBTdHJhbmdlbHlcclxuICAgICAgLy8gZW5vdWdoLCAuYXBwZW5kIGFjY29tcGxpc2hlcyB0aGlzIGZvciB1cy5cclxuICAgICAgdHJzID0gJC5tYXAoY29sdW1uLCBmdW5jdGlvbihrdikgeyByZXR1cm4ga3ZbMV07IH0pO1xyXG4gICAgICAkdGFibGUuY2hpbGRyZW4oXCJ0Ym9keVwiKS5hcHBlbmQodHJzKTtcclxuXHJcbiAgICAgIC8vIFJlc2V0IHNpYmxpbmdzXHJcbiAgICAgICR0YWJsZS5maW5kKFwidGhcIikuZGF0YShcInNvcnQtZGlyXCIsIG51bGwpLnJlbW92ZUNsYXNzKFwic29ydGluZy1kZXNjIHNvcnRpbmctYXNjXCIpO1xyXG4gICAgICAkdGhpc190aC5kYXRhKFwic29ydC1kaXJcIiwgc29ydF9kaXIpLmFkZENsYXNzKFwic29ydGluZy1cIitzb3J0X2Rpcik7XHJcblxyXG4gICAgICAkdGFibGUudHJpZ2dlcihcImFmdGVydGFibGVzb3J0XCIsIHtjb2x1bW46IHRoX2luZGV4LCBkaXJlY3Rpb246IHNvcnRfZGlyfSk7XHJcbiAgICAgICR0YWJsZS5jc3MoXCJkaXNwbGF5XCIpO1xyXG4gICAgfSwgMTApO1xyXG5cclxuICAgIHJldHVybiAkdGhpc190aDtcclxuICB9O1xyXG5cclxuICAvLyBDYWxsIG9uIGEgc29ydGFibGUgdGQgdG8gdXBkYXRlIGl0cyB2YWx1ZSBpbiB0aGUgc29ydC4gVGhpcyBzaG91bGQgYmUgdGhlXHJcbiAgLy8gb25seSBtZWNoYW5pc20gdXNlZCB0byB1cGRhdGUgYSBjZWxsJ3Mgc29ydCB2YWx1ZS4gSWYgeW91ciBkaXNwbGF5IHZhbHVlIGlzXHJcbiAgLy8gZGlmZmVyZW50IGZyb20geW91ciBzb3J0IHZhbHVlLCB1c2UgalF1ZXJ5J3MgLnRleHQoKSBvciAuaHRtbCgpIHRvIHVwZGF0ZVxyXG4gIC8vIHRoZSB0ZCBjb250ZW50cywgQXNzdW1lcyBzdHVwaWR0YWJsZSBoYXMgYWxyZWFkeSBiZWVuIGNhbGxlZCBmb3IgdGhlIHRhYmxlLlxyXG4gICQuZm4udXBkYXRlU29ydFZhbCA9IGZ1bmN0aW9uKG5ld19zb3J0X3ZhbCl7XHJcbiAgdmFyICR0aGlzX3RkID0gJCh0aGlzKTtcclxuICAgIGlmKCR0aGlzX3RkLmlzKCdbZGF0YS1zb3J0LXZhbHVlXScpKXtcclxuICAgICAgLy8gRm9yIHZpc3VhbCBjb25zaXN0ZW5jeSB3aXRoIHRoZSAuZGF0YSBjYWNoZVxyXG4gICAgICAkdGhpc190ZC5hdHRyKCdkYXRhLXNvcnQtdmFsdWUnLCBuZXdfc29ydF92YWwpO1xyXG4gICAgfVxyXG4gICAgJHRoaXNfdGQuZGF0YShcInNvcnQtdmFsdWVcIiwgbmV3X3NvcnRfdmFsKTtcclxuICAgIHJldHVybiAkdGhpc190ZDtcclxuICB9O1xyXG5cclxuICAvLyAtLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS1cclxuICAvLyBEZWZhdWx0IHNldHRpbmdzXHJcbiAgLy8gLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tXHJcbiAgJC5mbi5zdHVwaWR0YWJsZS5kaXIgPSB7QVNDOiBcImFzY1wiLCBERVNDOiBcImRlc2NcIn07XHJcbiAgJC5mbi5zdHVwaWR0YWJsZS5kZWZhdWx0X3NvcnRfZm5zID0ge1xyXG4gICAgXCJpbnRcIjogZnVuY3Rpb24oYSwgYikge1xyXG4gICAgICByZXR1cm4gcGFyc2VJbnQoYSwgMTApIC0gcGFyc2VJbnQoYiwgMTApO1xyXG4gICAgfSxcclxuICAgIFwiZmxvYXRcIjogZnVuY3Rpb24oYSwgYikge1xyXG4gICAgICByZXR1cm4gcGFyc2VGbG9hdChhKSAtIHBhcnNlRmxvYXQoYik7XHJcbiAgICB9LFxyXG4gICAgXCJzdHJpbmdcIjogZnVuY3Rpb24oYSwgYikge1xyXG4gICAgICByZXR1cm4gYS5sb2NhbGVDb21wYXJlKGIpO1xyXG4gICAgfSxcclxuICAgIFwic3RyaW5nLWluc1wiOiBmdW5jdGlvbihhLCBiKSB7XHJcbiAgICAgIGEgPSBhLnRvTG9jYWxlTG93ZXJDYXNlKCk7XHJcbiAgICAgIGIgPSBiLnRvTG9jYWxlTG93ZXJDYXNlKCk7XHJcbiAgICAgIHJldHVybiBhLmxvY2FsZUNvbXBhcmUoYik7XHJcbiAgICB9XHJcbiAgfTtcclxufSkoalF1ZXJ5KTtcclxuIl19
