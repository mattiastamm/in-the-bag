# Auth Page (Login & Signup)

## SECTION: LOGIN

### -- Test 1: Valid Login --
- **Page**: Auth - Login
- **Flow**: User logs in with valid credentials
- **Test Name**: Login with correct email and password
- **Risk/Priority**: High

**Test Steps**:
1. Navigate to the login page
2. Enter valid email and password
3. Click "Log In"

**Expected Result**: User is logged in and redirected to homepage  
**Actual Result**: User is logged in and redirected to homepage  
**Result**: Pass

### -- Test 2: Login with empty fields --
- **Page**: Auth - Login
- **Flow**: User submits login form without filling fields
- **Test Name**: Submit empty login form
- **Risk/Priority**: Medium

**Test Steps**:
1. Navigate to login page
2. Leave both fields empty
3. Click "Log In"

**Expected Result**: Validation message shown: "Password is required" or "Email is required"
**Actual Result**: Validation message shown: "Password is required"  
**Result**: Pass


### -- Test 3: Invalid login credentials --
- **Page**: Auth - Login
- **Flow**: User logs in with incorrect credentials
- **Test Name**: Login with wrong email or password
- **Risk/Priority**: High

**Test Steps**:
1. Navigate to login page
2. Enter incorrect email/password combination
3. Click "Log In"

**Expected Result**: Error message: "Invalid credentials"  
**Actual Result**: Error message: "Invalid credentials"  
**Result**: Pass

---

## SECTION: SIGNUP

### -- Test 4: Valid Signup --
- **Page**: Auth - Signup
- **Flow**: User signs up with valid info
- **Test Name**: Valid new account creation
- **Risk/Priority**: High

**Test Steps**:
1. Navigate to signup page
2. Enter valid email, password, and matching confirm password
3. Click "Create Account"

**Expected Result**: Account is created and user is logged in  
**Actual Result**: Account is created and user is logged in  
**Result**: Pass


### -- Test 5: Empty signup fields --
- **Page**: Auth - Signup
- **Flow**: User tries to signup with empty form
- **Test Name**: Submit blank signup form
- **Risk/Priority**: Medium

**Test Steps**:
1. Navigate to signup page
2. Leave all fields empty
3. Click "Create Account"

**Expected Result**: Validation message shown: "Email and password are required"  
**Actual Result**: Validation message shown: "Email and password are required"  
**Result**: Pass


### -- Test 6: Password too short --
- **Page**: Auth - Signup
- **Flow**: User tries short password
- **Test Name**: Password length below 4 characters
- **Risk/Priority**: Medium

**Test Steps**:
1. Enter valid email, password with 3 characters, and match confirm password
2. Click "Create Account"

**Expected Result**: Message: "Password must be between 4 and 16 characters"  
**Actual Result**: Message: "Password must be between 4 and 16 characters"  
**Result**: Pass


### -- Test 7: Password too long --
- **Page**: Auth - Signup
- **Flow**: User tries long password
- **Test Name**: Password length over 16 characters
- **Risk/Priority**: Medium

**Test Steps**:
1. Enter valid email, password with 17+ characters, and match confirm password
2. Click "Create Account"

**Expected Result**: Message: "Password must be between 4 and 16 characters"  
**Actual Result**: Message: "Password must be between 4 and 16 characters"  
**Result**: Pass


### -- Test 8: Invalid email format --
- **Page**: Auth - Signup
- **Flow**: User enters bad email
- **Test Name**: Email without "@" or domain
- **Risk/Priority**: Medium

**Test Steps**:
1. Enter invalid email like "userexample.com"
2. Enter valid password and match confirm password
3. Click "Create Account"

**Expected Result**: Message: "Must be a valid email"  
**Actual Result**: Message: "Must be a valid email"  
**Result**: Pass


### -- Test 9: Confirm password mismatch --
- **Page**: Auth - Signup
- **Flow**: Password and confirm password differ
- **Test Name**: Mismatched confirm password
- **Risk/Priority**: Medium

**Test Steps**:
1. Enter valid email
2. Enter password and different confirm password
3. Click "Create Account"

**Expected Result**: Message: "Passwords do not match"  
**Actual Result**: Message: "Passwords do not match"  
**Result**: Pass


### -- Test 10: Email already exists --
- **Page**: Auth - Signup
- **Flow**: User tries to sign up with existing email
- **Test Name**: Duplicate email registration
- **Risk/Priority**: High

**Test Steps**:
1. Enter an email that already exists
2. Enter valid password and match confirm password
3. Click "Create Account"

**Expected Result**: Message: "Email is already in use"  
**Actual Result**: Message: "Email is already in use"  
**Result**: Pass


# Inventory Page

## SECTION: Add New Disc

### -- Test 1: Add new disc successfully --
- **Page**: Inventory
- **Flow**: User adds a new disc with all required info
- **Test Name**: Add "Destroyer" to inventory
- **Risk/Priority**: High

**Test Steps**:
1. Click "Add Disc"
2. Search for "Destroyer" and select it from search
3. Choose plastic from dropdown (e.g. Star)
4. Enter weight (e.g. 175g)
5. Click "Add Disc"

**Expected Result**: Disc is added to inventory and visible in disc list  
**Actual Result**: Disc is added to inventory and visible in disc list  
**Result**: Pass


### -- Test 2: Add disc without plastic or weight --
- **Page**: Inventory
- **Flow**: User submits incomplete disc info
- **Test Name**: Add disc without plastic or weight
- **Risk/Priority**: Medium

**Test Steps**:
1. Click "Add Disc"
2. Search and select "Destroyer"
3. Do NOT select plastic or weight
4. Click "Add Disc"

**Expected Result**: Message shown: "Please fill in all required fields and ensure weight is greater than 0."  
**Actual Result**: Message shown: "Please fill in all required fields and ensure weight is greater than 0."  
**Result**: Pass


### -- Test 3: Edit flight numbers before adding --
- **Page**: Inventory
- **Flow**: User modifies flight numbers before saving
- **Test Name**: Add disc with custom flight numbers
- **Risk/Priority**: Medium

**Test Steps**:
1. Click "Add Disc"
2. Search and select "Destroyer"
3. Select plastic and weight
4. Update speed, glide, turn, fade fields manually
5. Click "Add Disc"

**Expected Result**: Disc is added to inventory with custom flight numbers  
**Actual Result**: Disc is added to inventory with custom flight numbers  
**Result**: Pass

---

## SECTION: Remove Disc

### -- Test 4: Remove disc successfully --
- **Page**: Inventory
- **Flow**: User removes a disc from inventory
- **Test Name**: Delete disc from modal
- **Risk/Priority**: High

**Test Steps**:
1. Click on a disc to open details modal
2. Click "Delete"
3. Confirm in alert: "Are you sure you want to delete Destroyer?"
4. Click OK

**Expected Result**: Disc is deleted and no longer visible in list  
**Actual Result**: Disc is deleted and no longer visible in list  
**Result**: Pass


### -- Test 5: Cancel delete operation --
- **Page**: Inventory
- **Flow**: User cancels deletion
- **Test Name**: Cancel disc deletion
- **Risk/Priority**: Low

**Test Steps**:
1. Open disc details modal
2. Click "Delete"
3. In the alert popup, click "Cancel"

**Expected Result**: Deletion is canceled, modal remains open  
**Actual Result**: Deletion is canceled, modal remains open  
**Result**: Pass

---

## SECTION: Update Disc Info

### -- Test 6: Update disc weight --
- **Page**: Inventory
- **Flow**: User updates disc weight
- **Test Name**: Change disc weight to 200g
- **Risk/Priority**: Medium

**Test Steps**:
1. Open disc details modal
2. Change weight field to 200
3. Click "Save"

**Expected Result**: Disc updates successfully and now shows 200g  
**Actual Result**: Disc updates successfully and now shows 200g  
**Result**: Pass


# My Bags Page

## SECTION: Adding a Bag

### -- Test 1: Add bag successfully --
- **Page**: My Bags
- **Flow**: User creates a new bag
- **Test Name**: Add a new bag
- **Risk/Priority**: High

**Test Steps**:
1. Click "Add Bag"
2. Modal opens
3. Enter bag name (e.g. "Tournament Bag")
4. Click "Save"

**Expected Result**: Bag appears in the list  
**Actual Result**: Bag appears in the list  
**Result**: Pass


### -- Test 2: Add bag without name --
- **Page**: My Bags
- **Flow**: User submits without entering a name
- **Test Name**: Save bag with empty name
- **Risk/Priority**: Medium

**Test Steps**:
1. Click "Add Bag"
2. Leave the bag name field empty
3. Click "Save"

**Expected Result**: Alert shown: "Please enter a bag name"  
**Actual Result**: Alert shown: "Please enter a bag name"  
**Result**: Pass

---

## SECTION: Editing a Bag

### -- Test 3: Edit bag and select new discs --
- **Page**: My Bags
- **Flow**: User edits which discs are assigned to the selected bag
- **Test Name**: Add and remove discs from existing bag
- **Risk/Priority**: High

**Test Steps**:
1. Select a bag
2. Click "Edit Bag"
3. In modal, check discs you want to include and uncheck the ones you want to remove
4. Click "Save Changes"

**Expected Result**: Bag updates and only selected discs remain  
**Actual Result**: Bag updates and only selected discs remain  
**Result**: Pass


### -- Test 4: Unselect all discs from bag --
- **Page**: My Bags
- **Flow**: User removes all discs from a bag
- **Test Name**: Remove all discs from a bag
- **Risk/Priority**: Low

**Test Steps**:
1. Click "Edit Bag"
2. Uncheck all disc selections
3. Click "Save Changes"

**Expected Result**: Bag is now empty  
**Actual Result**: Bag is now empty  
**Result**: Pass

---

## SECTION: Deleting a Bag

### -- Test 5: Delete bag successfully --
- **Page**: My Bags
- **Flow**: User deletes an existing bag
- **Test Name**: Delete bag
- **Risk/Priority**: High

**Test Steps**:
1. Select bag to delete
2. Click "Delete Bag"
3. Alert appears: "Are you sure you want to delete this bag?"
4. Click "OK"

**Expected Result**: Bag is removed from list  
**Actual Result**: Bag is removed from list  
**Result**: Pass


### -- Test 6: Cancel bag deletion --
- **Page**: My Bags
- **Flow**: User cancels deletion
- **Test Name**: Cancel delete bag operation
- **Risk/Priority**: Low

**Test Steps**:
1. Select bag to delete
2. Click "Delete Bag"
3. In confirmation alert, click "Cancel"

**Expected Result**: Deletion is canceled, bag remains visible  
**Actual Result**: Deletion is canceled, bag remains visible  
**Result**: Pass

---

## SECTION: Getting Suggestions for a Bag

### -- Test 7: View and add suggestions to wishlist --
- **Page**: My Bags
- **Flow**: User views suggestions and adds selected discs to wishlist
- **Test Name**: Suggest discs and add to wishlist
- **Risk/Priority**: High

**Test Steps**:
1. Select a bag
2. Click "Suggest Discs"
3. Modal shows "Analyzing your bag..." briefly
4. One or two suggestion categories appear with disc options
5. Select 1 or more discs
6. Click "Add to Wishlist"

**Expected Result**: Selected discs are added to wishlist  
**Actual Result**: Selected discs are added to wishlist  
**Result**: Pass


### -- Test 8: Close suggestion modal without adding --
- **Page**: My Bags
- **Flow**: User closes modal without adding any discs
- **Test Name**: View suggestions but do nothing
- **Risk/Priority**: Low

**Test Steps**:
1. Click "Suggest Discs"
2. Wait for suggestions to load
3. Do not select any discs
4. Click "Close"

**Expected Result**: Modal closes, returns to My Bags page  
**Actual Result**: Modal closes, returns to My Bags page  
**Result**: Pass


### -- Test 9: Prevent duplicate wishlist entries --
- **Page**: My Bags
- **Flow**: Try adding already-wishlisted disc again
- **Test Name**: Suggest disc already in wishlist
- **Risk/Priority**: Medium

**Test Steps**:
1. Click "Suggest Discs"
2. Select discs that are already in the wishlist
3. Click "Add to Wishlist"

**Expected Result**: No duplicates added; no error  
**Actual Result**: No duplicates added; no error  
**Result**: Pass

---

## SECTION: Suggestion Engine

### -- Test 10: Suggestion Engine test 1 --
- **Page**: My Bags
- **Flow**: Suggest correct discs for Bag1
- **Test Name**: Getting suggestions for Bag1
- **Risk/Priority**: High

**Test Steps**:
1. Create a bag called Bag1
2. Select Bag1
3. Edit the Bag by adding a stable putt&approach disc, a stable midrange and an understable low-speed fairway driver
4. Press Suggest Discs

**Expected Result**: Suggestions are: overstable midrange and stable low-speed fairway driver  
**Actual Result**: Suggestions are: overstable midrange and stable low-speed fairway driver  
**Result**: Pass


### -- Test 11: Suggestion Engine test 2 --
- **Page**: My Bags
- **Flow**: Suggest correct discs for Bag2
- **Test Name**: Getting suggestions for Bag2
- **Risk/Priority**: High

**Test Steps**:
1. Create a bag called Bag2
2. Select Bag2
3. Edit the Bag by adding a stable putt&approach disc, an understable low-speed fairway driver, an overstable midrange, and a stable low-speed fairway driver
4. Press Suggest Discs

**Expected Result**: Suggestions are: stable midrange and an overstable approach disc  
**Actual Result**: Suggestions are: stable midrange and an overstable approach disc  
**Result**: Pass


### -- Test 12: Suggestion Engine test 3 --
- **Page**: My Bags
- **Flow**: Suggest correct discs for Bag3
- **Test Name**: Getting suggestions for Bag3
- **Risk/Priority**: High

**Test Steps**:
1. Create a bag called Bag3
2. Select Bag3
3. Edit the Bag by adding an understable putt&approach disc, an understable midrange, an overstable midrange, a stable low-speed fairway driver
4. Press Suggest Discs

**Expected Result**: Suggestions are: understable low-speed fairway driver and an overstable approach disc  
**Actual Result**: Suggestions are: understable low-speed fairway driver and an overstable approach disc  
**Result**: Pass


# Wishlist Page

## SECTION: Add to Inventory

### -- Test 1: Add wishlist disc to inventory (normal flow) --
- **Page**: Wishlist
- **Flow**: User adds a wishlist disc to inventory with default values
- **Test Name**: Add disc from wishlist to inventory
- **Risk/Priority**: High

**Test Steps**:
1. Press "Add to Inv" on a wishlist disc
2. Modal opens
3. Select plastic from dropdown
4. Enter weight (e.g. 173g)
5. Click "Add Disc"

**Expected Result**: Disc is removed from Wishlist and appears in Inventory  
**Actual Result**: Disc is removed from Wishlist and appears in Inventory  
**Result**: Pass


### -- Test 2: Add to inventory without plastic or weight --
- **Page**: Wishlist
- **Flow**: User submits incomplete info in modal
- **Test Name**: Add to inventory with missing required fields
- **Risk/Priority**: Medium

**Test Steps**:
1. Press "Add to Inv" on a wishlist disc
2. Modal opens
3. Do NOT select plastic or enter weight
4. Click "Add Disc"

**Expected Result**: Message displayed: "Please fill in all required fields and ensure weight is greater than 0."  
**Actual Result**: Message displayed: "Please fill in all required fields and ensure weight is greater than 0."  
**Result**: Pass


### -- Test 3: Add to inventory with custom flight numbers --
- **Page**: Wishlist
- **Flow**: User customizes flight numbers before adding to inventory
- **Test Name**: Add wishlist disc with customized flight numbers
- **Risk/Priority**: Medium

**Test Steps**:
1. Press "Add to Inv" on a wishlist disc
2. Modal opens
3. Select plastic and enter weight
4. Edit speed, glide, turn, fade fields
5. Click "Add Disc"

**Expected Result**: Disc is removed from Wishlist and appears in Inventory with custom flight numbers  
**Actual Result**: Disc is removed from Wishlist and appears in Inventory with custom flight numbers  
**Result**: Pass

---

## SECTION: Remove from Wishlist

### -- Test 4: Remove wishlist disc --
- **Page**: Wishlist
- **Flow**: User removes a disc from their wishlist
- **Test Name**: Remove disc from wishlist
- **Risk/Priority**: High

**Test Steps**:
1. Press "Remove" on a wishlist disc
2. Modal appears: "Are you sure you want to remove *discName* from your wishlist?"
3. Click "OK"

**Expected Result**: Disc is removed from Wishlist  
**Actual Result**: Disc is removed from Wishlist  
**Result**: Pass


### -- Test 5: Cancel remove from wishlist --
- **Page**: Wishlist
- **Flow**: User cancels deletion
- **Test Name**: Cancel wishlist disc removal
- **Risk/Priority**: Low

**Test Steps**:
1. Press "Remove" on a wishlist disc
2. Modal appears
3. Click "Cancel"

**Expected Result**: Modal closes, disc is not removed  
**Actual Result**: Modal closes, disc is not removed  
**Result**: Pass


# Profile Page

## SECTION: Change Password

### -- Test 1: Change password successfully --
- **Page**: Profile
- **Flow**: User updates password with valid input
- **Test Name**: Change password
- **Risk/Priority**: High

**Test Steps**:
1. Click profile icon in top-right corner
2. Click "Profile"
3. Click "Change Password"
4. In modal, enter:
   - Current password: `currentPass123`
   - New password: `newPass456`
   - Confirm new password: `newPass456`
5. Click "Change Password"

**Expected Result**: Message: "Password changed successfully!" and password has been changed
**Actual Result**: Message: "Password changed successfully!" and password has been changed
**Result**: Pass


### -- Test 2: Leave password fields empty --
- **Page**: Profile
- **Flow**: User leaves one or more fields blank
- **Test Name**: Empty change password form
- **Risk/Priority**: Medium

**Test Steps**:
1. Click profile icon in top-right corner
2. Click "Profile"
3. Click "Change Password"
4. Leave one or more fields empty
5. Click "Change Password"

**Expected Result**: Message: "Please fill in all of the fields."  
**Actual Result**: Message: "Please fill in all of the fields."  
**Result**: Pass


### -- Test 3: New password too short --
- **Page**: Profile
- **Flow**: User enters new password < 4 characters
- **Test Name**: New password too short
- **Risk/Priority**: Medium

**Test Steps**:
1. Click profile icon in top-right corner
2. Click "Profile"
3. Click "Change Password"
4. Enter valid current password
5. Enter `123` in both new password fields
6. Click "Change Password"

**Expected Result**: Message: "New password must be between 4 and 16 characters"  
**Actual Result**: Message: "New password must be between 4 and 16 characters"  
**Result**: Pass


### -- Test 4: Incorrect current password --
- **Page**: Profile
- **Flow**: User enters wrong current password
- **Test Name**: Current password incorrect
- **Risk/Priority**: High

**Test Steps**:
1. Click profile icon in top-right corner
2. Click "Profile"
3. Click "Change Password"
4. Enter incorrect current password: `wrongpass`
5. Enter new password twice: `newPass123`
6. Click "Change Password"

**Expected Result**: Message: "Current password is incorrect"  
**Actual Result**: Message: "Current password is incorrect"  
**Result**: Pass


### -- Test 5: New passwords don't match --
- **Page**: Profile
- **Flow**: Mismatched new password and confirmation
- **Test Name**: Confirm password mismatch
- **Risk/Priority**: Medium

**Test Steps**:
1. Click profile icon in top-right corner
2. Click "Profile"
3. Click "Change Password"
4. Enter valid current password
5. New password: `pass1234`, Confirm password: `pass1235`
6. Click "Change Password"

**Expected Result**: Message: "New passwords do not match."  
**Actual Result**: Message: "New passwords do not match."  
**Result**: Pass

---

## SECTION: Delete Account

### -- Test 6: Delete account successfully --
- **Page**: Profile
- **Flow**: User deletes account after password confirmation
- **Test Name**: Delete account
- **Risk/Priority**: High

**Test Steps**:
1. Click profile icon in top-right corner
2. Click "Profile"
3. Click "Delete Account"
4. In modal, enter correct current password
5. Click "Delete Account"

**Expected Result**: Account is deleted, user is logged out  
**Actual Result**: Account is deleted, user is logged out  
**Result**: Pass


### -- Test 7: Incorrect password on account deletion --
- **Page**: Profile
- **Flow**: User tries to delete account with wrong password
- **Test Name**: Delete account with invalid password
- **Risk/Priority**: High

**Test Steps**:
1. Click profile icon in top-right corner
2. Click "Profile"
3. Click "Delete Account"
4. Enter wrong password
5. Click "Delete Account"

**Expected Result**: Message: "Current password is incorrect"  
**Actual Result**: Message: "Current password is incorrect"  
**Result**: Pass

---

## SECTION: Log Out

### -- Test 8: Log out --
- **Page**: Header
- **Flow**: User logs out of account
- **Test Name**: Successful logout
- **Risk/Priority**: High

**Test Steps**:
1. Click profile icon in top-right
2. Click "Log Out"

**Expected Result**: User is logged out and redirected to login page  
**Actual Result**: User is logged out and redirected to login page  
**Result**: Pass
