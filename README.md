# square_repos

Android application that lists Square’s public open source repositories on GitHub.

======= Technical considerations

Native android app developed in kotlin 
Min SDK 19

It supports both device orientations (portrait and Landscape)

It supports tablets and phone, with different layouts for each one. Split screen for tablets/phone-landscape and separated screens to phone-portrait

It supports offline operation

Libraries:

    Dagger 2: Creates, manages and inject data access classes in the application
    Retrofit 2: Manages the communication with server
    Moshi: Parse json from server
    Rxjava 2: manage and map data from server and local database
    Room: To persist local data
    Stetho: To debugging communication between app and server

======= Main classes

DataRepository: This class manage the other two (NetworkApi and LocalDatabase) and provide data to the application.

ReposViewModel: ViewModel responsible to the communication between data providers and views. It communicate with 'DataRepository' class to read and write data and provide view states objects(ListRepoState and DetailRepoState) for the views

ReposActivity: Main class responsible to manage the navigation of the screens (list and details). In case of tablets and phone landscape, both screens are showed at the same time. In case of phones portrait, screens are showed once a time

RepoListFragment: Responsable to show the list of repositories

RepoDetailFragment : Responsable to show details of a repository
