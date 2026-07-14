# Changes made when integrating `factory-pattern` (Farid) into the main project

Source: https://github.com/SoftwareDesign-group10/factory-pattern

## 🐛 Bug fix: `userName` / `accountType` were getting swapped

**File:** `UniversityUser.java`

Student, Staff, and Faculty all call their superclass constructor as:
```java
super(email, password, accountType, userName);
```
But `UniversityUser`'s constructor was declared as:
```java
public UniversityUser(String email, String password, String userName, String accountType)
```
Because Java binds constructor arguments by position, not by name, `accountType`
was landing in the `userName` slot and vice versa — every University-type
account (Student/Staff/Faculty; **not** Partner, which extends `RegisteredUser`
directly and wasn't affected) ended up with these two fields swapped.

**Verified empirically** — before the fix, creating a Student with
`userName="Alice Smith"`, `accountType="STUDENT"` produced:
```
getUserName()      -> "STUDENT"       (wrong)
accountType field  -> "Alice Smith"   (wrong)
```
After the fix:
```
getUserName()      -> "Alice Smith"   (correct)
accountType field  -> "STUDENT"       (correct)
```

**Fix:** changed `UniversityUser`'s constructor parameter order to
`(email, password, accountType, userName)` to match what its subclasses
actually pass. No changes needed in Student/Staff/Faculty themselves.

## ➕ Added: `Partner` support in the factory

`RegisteredUserFactory.createUser()` only had branches for `STUDENT`, `STAFF`,
`FACULTY` — passing `"PARTNER"` fell through to the `IllegalArgumentException`
at the end. Since Req1/Req3 require partner accounts, added:
```java
else if (accountType.equalsIgnoreCase("PARTNER")) {
    return new Partner(email, password, accountType, userName);
}
```
Also added `PARTNER` to `AccountValidator.validateAccountType()`'s allow-list
(same issue — it would have rejected Partner registrations).

## ✏️ Renamed: `AccountManagement` → `AccountValidator`

Farid's `RegisteredUserFactory` internally creates its own `AccountManagement`
just to run format validation (email regex, password length, etc.). The main
project already has a different, unrelated `AccountManagement` class (the
diagram-level one with `createAccount`/`verifyUniqueEmail`/persistence via
`UserRepository`) living in the same package — a same-name collision.
Renamed Farid's version to `AccountValidator`. Logic is untouched, just the
class name and the internal reference inside `RegisteredUserFactory`.

## ➕ Added: `organizationId` getter/setter + overloaded `createUser()`

`RegisteredUser` had a `private long organizationId` field that was never
settable from outside the class — no constructor parameter, no setter. The
CSV persistence layer needs to save/reload a student ID or organization ID
per user (this is what distinguishes pricing/verification per Req1/Req3), so:

- Added `getOrganizationId()` / `setOrganizationId(long)` to `RegisteredUser`
- Added an **overload** of `createUser()` on the factory:
  `createUser(email, password, accountType, userName, long organizationId)`
  — calls the original method, then sets the id. The original 4-arg
  `createUser()` is untouched.

**Team should confirm** this is the right place to collect the id — an
alternative would be putting it on `AccountManagement.createAccount()`
instead of threading it through the factory.

## ➕ Added: public getters on `RegisteredUser`

`accountType` and `hourlyRate` were `protected` fields with no public getter.
`CsvUserRepository` (needs `accountType` for the CSV column) and
`BookingManager` (needs `hourlyRate` to calculate the deposit) both live
outside this class hierarchy, so added:
```java
public String getAccountType() { return accountType; }
public double getHourlyRate() { return hourlyRate; }
```

## 🔤 Naming: `getEmail()` not `getEmailAddress()`

Farid's `RegisteredUser.getEmail()` differs from the placeholder method name
I'd used earlier (`getEmailAddress()`). Updated all callers across the
project (`BookingManager`, `GUIController`, `AccountManagement`,
`CsvUserRepository`) to use `getEmail()`.

## Still open (not fixed here — needs team discussion)

- No email **uniqueness** check anywhere in `AccountValidator`/factory —
  only format is checked. Uniqueness is handled separately in the project's
  `AccountManagement.verifyUniqueEmail()`, so this is probably fine as-is,
  but worth confirming it's not expected in the factory's validation too.
- Password strength: `AccountValidator` only checks length ≥ 8. Req1 asks
  for uppercase/lowercase/numbers/symbols — that stronger check currently
  lives in the project's `AccountManagement.validateStrongPassword()`
  instead, so functionally it's covered, just not in Farid's file.
- `verifyUniversityAccount()` (Req1's university verification step) still
  isn't implemented anywhere.
