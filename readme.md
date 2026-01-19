### Prerequisites

- Java 11+
- Maven 4.0.0+
- (Optional) Allure for reporting

### Configuration

- **Environment:** Set via VM option, e.g. `-Denv=dev`
- **Thread count:** Set in `PlayerControllerRegressionSuite.xml` (e.g., `thread-count="3"`)
- **Base URL:** Set in `config.properties` (e.g., `dev.baseUrl=http://localhost:8080`)

Service under the test: Player Controller API, Testing CRUD operations for player management.

### Running Tests

To run the tests, use the following Maven command:

mvn clean test -Denvironment=<env>

Replace `<env>` with your desired environment (e.g., `dev`).

Tests also can be run in multiple threads by running PlayerControllerRegressionSuite.xml via TestNG.

### Test Structure

The test suite includes:

- **API Tests:** Verify CRUD operations for player management.

### Test Flows

- **Create Player:**
    1. Send a GET request to create a new player.
    2. Verify the response status and body.

- **Get Player:**
    1. Send a POST request to retrieve player details.
    2. Verify the response status and body.

- **Update Player:**
    1. Send a PATCH request to update player details.
    2. Verify the response status and body.

- **Delete Player:**
    1. Send a DELETE request to remove a player.
    2. Verify the response status.
