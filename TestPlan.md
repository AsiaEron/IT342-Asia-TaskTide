# Software Test Plan for TaskTide

## 1. Introduction
### 1.1 Purpose
This test plan outlines the testing strategy for the TaskTide productivity management system, which includes a Spring Boot backend API, React web frontend, and Android mobile application.

### 1.2 Scope
The testing covers all functional requirements implemented across the three platforms.

### 1.3 Test Items
- Backend API (Spring Boot)
- Frontend Web Application (React)
- Mobile Application (Android)

## 2. Functional Requirements Coverage

### 2.1 User Management
- User Registration
- User Login
- Retrieve All Users

### 2.2 Task Management
- Create Task
- Retrieve User Tasks
- Update Task
- Delete Task

### 2.3 Frontend Features
- User Authentication (Login/Register)
- Dashboard with Task Management
- Task Filtering and Sorting

### 2.4 Mobile Features
- User Authentication
- Task Management Interface

## 3. Test Cases

### 3.1 Backend API Test Cases

#### User Registration
- TC001: Successful user registration with valid data
- TC002: Registration failure with duplicate email
- TC003: Registration failure with invalid email format
- TC004: Registration failure with missing required fields

#### User Login
- TC005: Successful login with valid credentials
- TC006: Login failure with invalid email
- TC007: Login failure with invalid password
- TC008: Login failure with empty credentials

#### Get All Users
- TC009: Retrieve all users successfully
- TC010: Handle empty user list

#### Create Task
- TC011: Successful task creation with valid data
- TC012: Task creation failure with invalid user
- TC013: Task creation failure with missing required fields

#### Get User Tasks
- TC014: Retrieve tasks for existing user
- TC015: Retrieve empty task list for user with no tasks
- TC016: Handle invalid user ID

#### Update Task
- TC017: Successful task update
- TC018: Update failure for non-existent task
- TC019: Update failure with invalid data

#### Delete Task
- TC020: Successful task deletion
- TC021: Delete failure for non-existent task

### 3.2 Frontend Test Cases

#### Login
- TC022: Successful login with valid credentials
- TC023: Login failure with invalid credentials
- TC024: Redirect to dashboard after login

#### Register
- TC025: Successful registration
- TC026: Registration failure with existing email
- TC027: Redirect to login after registration

#### Dashboard
- TC028: Display user tasks
- TC029: Add new task
- TC030: Edit existing task
- TC031: Delete task
- TC032: Filter tasks by status
- TC033: Filter tasks by energy level
- TC034: Sort tasks

### 3.3 Mobile Test Cases

#### Authentication
- TC035: Successful login
- TC036: Successful registration
- TC037: Authentication failure

#### Task Management
- TC038: View tasks
- TC039: Add task
- TC040: Edit task
- TC041: Delete task

## 4. Test Scripts / Test Steps

### 4.1 Backend API Test Scripts

#### TC001: Successful User Registration
1. Prepare valid user data (fname, lname, email, password)
2. Send POST request to /users/register
3. Verify response status 201
4. Verify response contains user_id, email, success message
5. Verify user is saved in database

#### TC005: Successful Login
1. Register a user first
2. Send POST request to /users/login with email and password
3. Verify response status 200
4. Verify response contains token, user_id, email

#### TC011: Create Task
1. Login to get token
2. Send POST request to /api/tasks with task data
3. Verify response status 200
4. Verify task is created with correct data

#### TC014: Get User Tasks
1. Create some tasks for a user
2. Send GET request to /api/tasks/user/{userId}
3. Verify response status 200
4. Verify response contains user's tasks

#### TC017: Update Task
1. Create a task
2. Send PUT request to /api/tasks/{taskId} with updated data
3. Verify response status 200
4. Verify task is updated in database

#### TC020: Delete Task
1. Create a task
2. Send DELETE request to /api/tasks/{taskId}
3. Verify response status 200
4. Verify task is deleted from database

### 4.2 Frontend Test Scripts

#### TC022: Successful Login
1. Navigate to /login
2. Enter valid email and password
3. Click login button
4. Verify redirect to /dashboard
5. Verify user is logged in

#### TC028: Display User Tasks
1. Login and navigate to dashboard
2. Verify tasks are displayed
3. Verify task details (name, description, energy level, status)

#### TC029: Add New Task
1. Click add task button
2. Fill task form (name, description, energy level)
3. Submit form
4. Verify task appears in list

#### TC030: Edit Task
1. Click edit on a task
2. Modify task details
3. Save changes
4. Verify task is updated

#### TC031: Delete Task
1. Click delete on a task
2. Confirm deletion
3. Verify task is removed from list

## 5. Automated Test Cases

### 5.1 Backend Automated Tests (JUnit)

#### AuthControllerTest
- testRegisterUser_Success
- testRegisterUser_DuplicateEmail
- testLogin_Success
- testLogin_InvalidCredentials

#### TaskControllerTest
- testAddTask_Success
- testGetUserTasks_Success
- testEditTask_Success
- testDeleteTask_Success

#### UserControllerTest
- testGetAllUsers_Success

### 5.2 Frontend Automated Tests (Jest/React Testing Library)

#### Auth Tests
- Login component rendering
- Register component rendering
- Form submission handling

#### Dashboard Tests
- Task list rendering
- Add task functionality
- Edit task functionality
- Delete task functionality
- Filtering functionality

### 5.3 Test Execution
Automated tests will be run using:
- Maven for backend (mvn test)
- npm for frontend (npm test)
- Gradle for mobile (./gradlew test)

## 6. Test Environment
- Backend: Spring Boot with H2 in-memory database for testing
- Frontend: Node.js with Jest
- Mobile: Android emulator with Espresso

## 7. Test Data
- Test users with various roles
- Sample tasks with different energy levels and statuses

## 8. Success Criteria
- All test cases pass
- No critical bugs found
- Code coverage > 80% for backend
- All functional requirements covered</content>
<parameter name="filePath">c:\Users\Eron Asia\Desktop\IT342-Asia-TaskTide\TestPlan.md