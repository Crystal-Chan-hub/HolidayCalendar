# Holiday Calendar
- The input API is provided by [holidayAPI](https://www.abstractapi.com/holidays-api).
- The output API is provided by [twillio](https://www.twilio.com/).
  - Report data will be sent as an SMS to a single configured number (need to be configured in a config file).
- A calendar will be displayed, in which the user can flip through (page-per-month view). When the user clicks on a day, it will retrieve the necessary information
  about the chosen day and display a ‘This is a holiday’ or ‘This is not a holiday’ message, and the day’s
  slot will be updated in that calendar with a visual indication of this state.

---
## Configuration files need to be modified before running
   * A directory named ***config*** in root directory:
     * holidayConfig.json
       * the **key** of Holiday API need to be put here.
     * twilioConfig.json
       * **AccountSid**, put the Twilio account SID here.
       * **AuthToken**, put the Twilio auth token here.
       * **From**, put the Twilio phone number in [E.164](https://www.twilio.com/docs/glossary/what-e164) format here, this is the number that the message will send from.
       * **To**, put the Twilio verified phone number, this is the destination phone number in [E.164](https://www.twilio.com/docs/glossary/what-e164) format for SMS/MMS.

## Running the application
   * To launch, use command `gradle run --args="[online/offline] [online/offline]"`, use either online or offline in the brackets with the first controlling the input API, and the second controlling the output API.
   * eg. `gradle run --args="online offline"`
   * Once launch, you will need to select a country and a threshold holiday count from the dropdown boxes and press ***Start*** button to start.
   * The calendar will start with the current month and year
   * On the top there is two buttons on each side of the month and year label.
     - ***<*** button allows you to go to the previous month.
     - ***>*** button allows you to go to the next month.
   * To get information for a day in that month
     - Click on the button of the date number
     - If the date is present in the database, a pop-up window will ask to choose if you want to retrieve the information about this date from the database or API. After selected, pressed ***Submit*** button to get the information described in the next dot point. If the date is not present in the database, the next step will execute.
     - A pop-up window will display either "‘This is a holiday" or "This is not a holiday" as the title of the window, with relevant information in the window.
     - At the same time, an indication on the day slot will be displayed with either ***'H'*** meaning this day is a holiday or ***'NH'*** meaning this day is not a holiday.
     - If the date selected has more matching holidays than the threshold, the display will blink.
     - Click on the ***Close*** button to close the pop-up window and continue using the app.
   * To send SMS message with a list of known public holidays for the current displaying month
     - At the bottom of the app, click on the ***Send*** button to send a list of known public holidays for the current displaying month.
     - A window will pop up with message saying the send status of your message, the body of the message and the numbers of the message sending to and from.
     - If a known day in this selected month has more matching holidays than the threshold, it will start with an asterisk (*).

## Notes for running Holiday API offline
   * Only the following days are considered as holiday within this API:
     * 01st,Jan
     * 24th,Dec
     * 25th,Dec
   * All the rest dates are considered as non-holiday in this API.

## Red-Green-Refactor process
1. Red: [fafbe36](https://github.sydney.edu.au/ccha8970/SCD2_2021_Exam/commit/fafbe361a3442dbc7f729b911f834ff8efa98f64)

   Green: [a78941f](https://github.sydney.edu.au/ccha8970/SCD2_2021_Exam/commit/a78941f0a3dcf07bcffde9997e0d276ef56ff0bb)

   Refactor: [df9b2fe](https://github.sydney.edu.au/ccha8970/SCD2_2021_Exam/commit/df9b2fee5cd7d7b650dd01b25bda293436583984)

2. Red: [a4bb459](https://github.sydney.edu.au/ccha8970/SCD2_2021_Exam/commit/a4bb459386b7726ec3eb07ed6b059f467c27f3f8)

   Green: [6623033](https://github.sydney.edu.au/ccha8970/SCD2_2021_Exam/commit/6623033d729beda121debc864c8ea882421ae116)

3. Red: [b473e0d](https://github.sydney.edu.au/ccha8970/SCD2_2021_Exam/commit/b473e0d85f2f763a2cf5d1da854e88b01265c66a)

   Green: [32d1fe2](https://github.sydney.edu.au/ccha8970/SCD2_2021_Exam/commit/32d1fe2142aa98c7696aac7d37fa97b47775811e)

4. Red: [cbd2587](https://github.sydney.edu.au/ccha8970/SCD2_2021_Exam/commit/cbd258764ffcf7e7180eb5a8610bbd3a84ce7862)

   Green: [fb1b9f2](https://github.sydney.edu.au/ccha8970/SCD2_2021_Exam/commit/fb1b9f2cdd79e6d4cc1bb08d3ffe65f0219cde52)

   Refactor [35cc8a2](https://github.sydney.edu.au/ccha8970/SCD2_2021_Exam/commit/35cc8a24e76b34911b0320cfd1ad02ba7946d594)

## Red-Green-Refactor process (cont. Exam extension)
5. Red: [5d612cb](https://github.sydney.edu.au/ccha8970/SCD2_2021_Exam/commit/5d612cb1c42577fe9c376b1efbadeec293168fa6)

   Green: [7ddc909](https://github.sydney.edu.au/ccha8970/SCD2_2021_Exam/commit/7ddc90902899f952ddab37f575af121c60f10aeb)

6. Red: [f957741](https://github.sydney.edu.au/ccha8970/SCD2_2021_Exam/commit/f957741e3eb2c1bb7e1bf958f2f096a7f05c7c9d)

   Green: [1c16c02](https://github.sydney.edu.au/ccha8970/SCD2_2021_Exam/commit/1c16c02fd6bc0eb59db3f42cafe79b93034d40aa)
