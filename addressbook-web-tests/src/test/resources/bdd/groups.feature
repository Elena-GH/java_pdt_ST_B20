Feature: Groups

  Scenario Outline: Group creation
    Given a set of groups
    When I create a new group with name <name>, header <header> and footer <footer>
    Then the new set of groups is equal to the old set with the added group
    Examples:
    | name        | header        | footer        |
    | test name 1 | test header 1 | test footer 1 |
    | test name 2 | test header 2 | test footer 2 |