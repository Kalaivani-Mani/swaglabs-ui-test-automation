# 🧪 SwagLabs UI Test Automation Framework

This repository contains an automated UI testing framework for the [SwagLabs (SauceDemo)](https://www.saucedemo.com/) e-commerce demo site. The framework is built using **Selenium WebDriver**, **TestNG**, **Maven**, and follows the **Page Object Model (POM)** design pattern. It generates detailed and interactive test reports using **ExtentReports** and **Allure**.

---

## 🚀 Project Overview

### 📂 GitHub Repository
- **Repository**: [Kalaivani-Mani/swaglabs-ui-test-automation](https://github.com/Kalaivani-Mani/swaglabs-ui-test-automation)  
- **Author**: [Kalaivani Mani](https://www.linkedin.com/in/kalaivani-m-8665b7181/)  

### 🌟 Key Features
- **Login Tests**: Valid and invalid login scenarios.
- **Inventory Validations**: Verifies product count, names, and details.
- **Cart Features**: Add/remove single or multiple items.
- **Visual Reporting**: Generates clean, detailed reports with **ExtentReports** and **Allure**.
- **Screenshots on Failure**: Captures screenshots for failed tests.
- **Data-Driven Testing**: Uses TestNG `DataProvider` for parameterized tests.
- **Scalable Architecture**: Implements the **Page Object Model (POM)** for maintainability.
- **Utility Layers**: Includes reusable utilities for waits, configuration, screenshots, and reporting.
- **CI/CD Ready**: Can be integrated with GitHub Actions or other CI tools.

---

## 🧰 Tech Stack

| **Tool/Library**    | **Purpose**                      |
|---------------------|----------------------------------|
| **Java**            | Core programming language        |
| **Selenium WebDriver** | Browser automation             |
| **TestNG**          | Test orchestration and execution |
| **Maven**           | Build and dependency management  |
| **WebDriverManager**| Automatic browser driver setup   |
| **ExtentReports**   | HTML-based test reporting        |
| **Allure**          | Interactive test dashboards      |
| **GitHub**          | Version control and collaboration|

---

## 🗂️ Project Structure

```bash
swaglabs-ui-test-automation/
│
├── src/
│   ├── main/java/
│   │   ├── base/              # Base test setup (WebDriver, ExtentReports)
│   │   ├── pages/             # Page Object classes for application pages
│   │   └── utils/             # Utility classes (config reader, waits, screenshots)
│   └── test/java/
│       └── tests/             # Test classes using TestNG
│
├── reports/                   # ExtentReports and screenshots
├── target/allure-results/     # Allure result files
├── pom.xml                    # Maven configuration file
└── README.md                  # Project documentation (this file)
```

---

## ⚙️ Prerequisites

Before running the tests, ensure you have the following installed:
1. **Java Development Kit (JDK)**: Version 8 or higher.
2. **Maven**: For dependency management and build.
3. **Browser Drivers**: Managed automatically by **WebDriverManager**.
4. **IDE**: IntelliJ IDEA, Eclipse, or Visual Studio Code for development.

---

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/Kalaivani-Mani/swaglabs-ui-test-automation.git
cd swaglabs-ui-test-automation
```

### 2. Install Dependencies
Run the following Maven command to download all dependencies:
```bash
mvn clean install
```

### 3. Configure `config.properties`
Update the `config.properties` file located in the `src/main/resources` directory:
```properties
baseUrl=https://www.saucedemo.com/
browser=chrome
default.wait.time=10
```

### 4. Run Tests
Run the tests using Maven:
```bash
mvn test
```

---

## 📊 Reporting

### 1. **ExtentReports**
- After test execution, the HTML report is generated in the `reports/` directory.
- Open the `extent-report.html` file in a browser to view the detailed report.

### 2. **Allure Reports**
- To generate and view Allure reports:
  ```bash
  allure serve target/allure-results
  ```

---

## 🧪 Test Scenarios

### 1. **Login Tests**
- Valid login with correct credentials.
- Invalid login with incorrect credentials.
- Locked-out user login.

### 2. **Inventory Page Validations**
- Verify product count matches the expected value.
- Validate product names and prices.

### 3. **Cart Features**
- Add a single item to the cart.
- Remove a single item from the cart.
- Add multiple items and clear the cart.

---

## 🛠️ Utilities

### 1. **ConfigReader**
- Reads configuration values (e.g., `baseUrl`, `browser`) from the `config.properties` file.

### 2. **WaitUtils**
- Provides reusable methods for explicit waits (e.g., waiting for visibility, clickability).

### 3. **ScreenshotUtil**
- Captures screenshots for failed tests and saves them in the `reports/screenshots/` directory.

### 4. **ExtentManager**
- Manages the singleton instance of ExtentReports for logging test results.

---

## 🧑‍💻 Contributing

Contributions are welcome! If you'd like to improve this project:
1. Fork the repository.
2. Create a feature branch.
3. Submit a pull request.

---

## 📜 License

This project is open-source and licensed under the [MIT License](LICENSE).  

You are free to use, modify, and distribute this project, provided that proper credit is given to the original author. This project was built using free and open-source tools, and no proprietary software was used.

## 📞 Contact

For any inquiries or feedback, feel free to reach out:
- **Author**: [Kalaivani Mani](https://www.linkedin.com/in/kalaivani-m-8665b7181/)
- **Email**: kalaivani2018.ece@gmail.com