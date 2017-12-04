# MovieSearchDemoApp

This app is intended to retrieve movies information from the [The Movie DB](https://www.themoviedb.org/) which is an open database for movies and series. Prior to run it, you need to create a TMDb account and request an API key. You can find all the information you need on the on [this link](https://www.themoviedb.org/documentation/api).

It was primarily developed for my learning purpose over one year ago, and now I just want to share it on the GitHub. For the GUI part, it uses some nice Material Design stuff such as TabLayout and  NavigationDrawer. It also uses MVP architecture and finally, for the HTTP calls,  it uses Retrofit library. It was based on [this project](https://github.com/thiagokimo/TMDb) (which in fact I found very useful), but I added some features to fit my needs. 

![Demo](https://user-images.githubusercontent.com/4574670/33531762-c53acd7a-d878-11e7-9f72-634c829aa725.gif)

## When running this app, you can filter results out by
  - Movies
    - Now Playing
	- Top Rated
	- UpComming
    - Popular
  - Series
    - On the Air
	- Top Rated
	- Popular
  - People 
  
## You can also use one of these genres on your queries (and easily you can add more genres):
  - Comedy
  - Adventure
  - Action
  - Documentary

Once you are on a movie/series details screen, you will see a "cast" view containing a list of the first five cast returned from the API. When clicking on an actor's name, you are redirected to the actor's filmography.
  
If you want to know more about my projects, please, visit my [blog](http://androidahead.com/). 
