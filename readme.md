# PG6102 ContExam Spring 2021

## Exam Result: Grade B

## Topic: Social Site Like Facebook

- This is my exam result for the ContExam in PG6102 Enterpriseprogramming in Spring 2021.

- Requirements through R1 to R3 are fully completed.

- In R4 I did try my best, but not entirely sure if it was enough for it to be completed.

- R5 I did not have enough time to complete. 

## API Endpoints for Messages

- `GET`: /api/messages/id for getting messages by ID, permitted only for registered users. Permitted to all.
- `POST`: /api/messages/id for creating new messages Permitted for Admins only
- `PUT`: /api/messages/id for replacing default messages Permitted for Admins only
- `DELETE`: /api/messages/id for deleting messages by Id Permitted for Admins only

## API Endpoints for User Collection for getting their Friendships

- `GET`: /api/user-collections/Id getting users collection of user friendships
- `PUT`: /api/user-collections/Id create User
- `PATCH`: /api/user-collections/Id Edit users Friendships

- I did not include DELETE or POST as POST was put with PUT and DELETE method was not needed for this entity.

## With E2E Tests

- TESTGETBOOKINGS
- TESTGETCOLLECTION
- TESTGETUSERSCOLLECTIONACCESSCONTROLL
- TESTCREATEUSER
- TESTUNAUTHORIZEDACCESS 
- TESTAMDQPRABBITSOCIAL

E2E are disabled at running `mvn clean verify` due to implication with Travis-Ci. If wanted to run, remove the @Disabled annotation at the top of the file.

## Tests

- All tests run green. 
- Coverage at `79%`

## Disclaimer

- Most of the code used in this repository are from Andrea Arcuri's Repository: https://github.com/arcuri82/testing_security_development_enterprise_systems
- I've added on each file to the correct file for when I've reused or modified code from the Repository








