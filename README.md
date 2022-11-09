# TicTacToe
TicTacToe with some AI

<img src="https://jamxgw.am.files.1drv.com/y4mF_dQhrNnVisf8GzBKSA7-AJDT_gFzqA04-RMqsr5oJ0t-djGqig4pmGK67SBPVJYvntqbYUUilX8bNVH413jBV1gVA1bgLKgkhnBXZ0SIYVPeFuHKgaGxy1GJHoKetJ95TtZje0wKiG8CZJhxVwKPVX-ZFoDShvTPU22-ynq2jciNEK6UiXH46UAwJWXfrlGy439PSkfXh6HMthKjny1HA?width=315&height=660&cropmode=none" width="200"> <img src="https://ytkqjg.am.files.1drv.com/y4mTcc_WmGtqY3BuOL_VlgpwyHIS-S6RKDEbVGjq9UovSFxjhbvip8qxGphxGlx6ERNUoLEaJUhT_AhXPU9Wpci8lQtxGV4WJoqEsYcERgz5qYAmloMxCrptvFnARSnBJLM87BIFXbHMkaBt-mWlj5JEClwvuEN_VkyuL4EH8SB3HPCsRoIZAjHiXxtRrOXfb8cEJH8DVFQzqVTiOx82pYajw?width=317&height=660&cropmode=none" width="200">




This app uses the MVC architecture. It takes little effort to add / remove a UI or feature.

The Controller class handles all of the events / callbacks and triggers:

  Cell selected,
  Volume up, Down,
  Next player turn,
  game ended (draw, player 1 won, player 2 won, etc)
  
All the UI controls or classes that need events first register themselves with the GameController. The GameController uses an array for each event type (e.g. NextPlayerTurn, GameStarted, GameEnded, etc) and loops through the array calling each callback for that specific event.

For example: the PlayerTurnDots class registers for the 'next plyer turn' event to know when to animate 'player changed' animation

The Model class stores all the data and logic for the game.

The View class handles all of the views and interaction (e.g. Grid for the actual game).

## Getting started

Have a look at the GameActivity.java on line 77. This should give an insite of how classes register themselves with the controller.

## The AI

There is some AI which helps decide the next best move. The TicTacToeAI.java is the AI class.

## The Grid

The ComboChecker.java class has the ability to check an 'unlimited-size-grid' for all possible outcomes.
