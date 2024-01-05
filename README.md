# PayWay Transactions
#### An Analytics Dashboard Android Application

This Android application serves as a standalone showcase for an analytics dashboard, specifically designed to visualize user payment data. The project emphasizes UI design, data handling, and API development. The assignment involved developing the application from scratch, including the design of a RESTful API interface for transaction data.

## Project Overview

### Functionality

* Implementation of a complete analytics dashboard featuring interactive charts and diagrams. Charts include;
    * Line Chart: By default, the chart shows two lines highlighting Deposits and Withdraws. When filters are applied, the filter criteria are highlighted at the bottom of the chart. **It is important to note that this graph merges the total amount of transactions per day.** This is so we can plot the data with fewer, legible, and more meaningful points.
    * Pie Chart: The Pie chart primarily presents the distribution of categories by percentage of the total amount for all transactions per category.
    * Radar Chart: The radar chart shows a distribution of the total Amounts of Deposits and withdraws across all the categories like Mobile Money, TV, etc

* Accurate data representation based on user-selected filters such as date, type, amount, and custom categories. Filters include;
    * Date: Allows for filtering with one or both start and end dates.
    * Type: Filtering for both Deposits/Withdraws.
    * Amount: Two seekbars to allow the user to select one or both minimum and maximum amounts.
    * Categories: A search bar/drop-down to allow selection of multiple categories

### User Interface

- Intuitive and user-friendly interface design following Material Design principles.


  **Loading Screen On Opening the app for the first time**
  
  <img src="https://github.com/RutaleIvanPaul/PayWay/assets/30496434/a4ffaf07-767c-40c1-b27b-ad43e06a0f1a" alt="screenshot" width="200" height="400"/>

  **Charts and Summary Information**


  <img src="https://github.com/RutaleIvanPaul/PayWay/assets/30496434/0f3f1c3b-0428-4ccd-a1f2-e324dd931277" alt="screenshot" width="200" height="400"/>
  <img src="https://github.com/RutaleIvanPaul/PayWay/assets/30496434/b6213018-99a4-4466-851b-45afab2d4e1a" alt="screenshot" width="200" height="400"/>
  <img src="https://github.com/RutaleIvanPaul/PayWay/assets/30496434/edd959db-e2c6-4acf-980b-6996b245c281" alt="screenshot" width="200" height="400"/>

  **Filter Screen**
  
   <img src="https://github.com/RutaleIvanPaul/PayWay/assets/30496434/d66a6656-0c9c-4ad1-93d9-2ed79a5ab067" alt="screenshot" width="200" height="400"/>

   
## Technical Details

### Technology Stack

- Kotlin programming language.
- Android SDK for application development.
- MVVM architecture for a clear separation of concerns.
- Clean architecture principles for modular and maintainable code.
- MPAndroidChart library for interactive charting and visualization.
- Coroutines for asynchronous and non-blocking programming.
- Mock API server using Postman to simulate transaction data.

## Building and Running the Project

1. Clone the repository to your local machine.
2. Open the main branch in Android Studio.
3. Build and run the application on an emulator or physical device.

## Additional Notes

**Project Improvements**
The application is already designed to be extensible, so easily open for the following;
- Pagination: Considering that the transactions are quite a number, paginating the response payload would allow for processing smaller chunks of the data at a time.
- Response Restructuring: Pagination is already a good solution, for bulky payloads, however, one solution could be avoiding the bulk altogether by restructuring the payload to only contain the data needed for display to the user. This reduces the need to process the bulk of the data within the application.
- Error Handling and Validation: Since this was for demonstration purposes, the effort has not been taken to validate user inputs or handle errors in a consistent manner across the application.
- Dependency Injection: For this project, the DI has been done manually in the App.kt file as the entry point of the application. For more production-oriented or larger projects, it would then make sense to use a DI Library like Dagger.
- Optimisation for the Production environment by increasing UI Accesibility following the Material Design principles, managing UI and app state in case of screen reconfiguration, and adding linting, code analysis, and continuous deployment tools.

---
