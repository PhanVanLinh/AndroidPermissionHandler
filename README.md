# PermissionHandler

A library which help you request runtime permission on Android easier.

### Feature
- Support request single/multiple permissions
- Handle show rationale
- Handle "Don't ask again"

### Install
```groovy

dependencies {
   ...
   implementation 'com.linh:permissionhandler:1.0.0'
}
```
### Usage
```java
RuntimePermission[] permissions = new RuntimePermission[] {  
        new RuntimePermission(Manifest.permission.READ_CONTACTS,  
                "We need READ_CONTACTS permission because ..."),
        new RuntimePermission(Manifest.permission.READ_CALENDAR, 
        "Pass your rationale message OR simple pass null if you don't want to show rationale")           
};  

new PermissionHandler.Builder(BasicSampleActivity.this, permissions)
             .setAllowRequestDontAskAgainPermission(true)
             .setListener(new RequestPermissionListener() {
            @Override
            public void onResult(RequestPermissionResult result) {
                if (result.isAllGranted()) {
                    // All requested permissions is granted
                } else if (result.isAllDenied()) {
                    // All requested permissions is denied
                } else {
                    for (RuntimePermission permission : result.getPermissions()) {
                        if (permission.getResult() == PackageManager.PERMISSION_GRANTED) {
                            // This permission is granted
                        } else {
                            // This permission is not granted
                        }
                    }
                }
            }
        }).build().request();
```

### Flow
Here is the current flow that I used for request runtime permission. If you see something which is not suitable, please suggest me
![flow image](/flow.png)

### Sample
Please check the [sample](/sample).  

### Contribute
Feel free to raise issue and pull request if you see something that should be improve.
Contributions and suggestions are always welcome. 
Also, you can try run some instrument test in sample for verify the solution

### License
```
Copyright 2018 Phan Van Linh

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```