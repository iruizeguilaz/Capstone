

############Requeriments###########

Analitycs: Add your id in the track_app.xml

  <string name="ga_trackingId">UA-xxxxx-2</string>

API SERIES: Add an api key for the series in the gradle.properties with the name API_SERIES_KEY

Example: API_SERIES_KEY = "XXXXXXXXXXXXXXXXX"

Api guide:
http://thetvdb.com/wiki/index.php?title=Programmers_API

############Awesome Series###########

This is an app to keep track of your favorite series. You can save them, mark the episodes viewed, and see all the information
about the serie as the actors, episodes, and so on.

It has a navigationview with three types of series that are explained later on. You can search series eassyly by name and save
them. Ather that when you load a serie saved, you only load the image from internet.

There is a refresh button in the detail serie fragment to update the data (if the sere is saved, the update data is saved again)

The database api brings a banner image with the name search, it is not until you do a ID search when you get a poster image, that
is why the search fragment swho a recycler with banner images, but my series fragment (pending, viewed, followed) shows the poster.

Analytycs and admons are implemented.

There is a home screen widget.


############Series types###########
There are 3 types of series: Pending, Followed, Viewed.

Pending: When you save a serie is added as pending until you mark a episode as viewed, in that momento the serie change into followed
Followed: The serie is in status followed while you are viewing it but you have not seing all episodes (if the serie has not finished yet, the serie is nor marked as viewed)
Viewed: if a serie is ended and you have marked all episodes as viewed, then it is changed into viewed

############Future releases###########

I will add these features along the next month

1- Notifications to be uptodate about the new episodes avaible (sync adapter)
2- Auto update information. I will add a sync adapter that periodically will update the series that you are following in order to check new episodes, poster and so on
3- Drive synchonization. I will add the posibility of doing backups in order to share your database between differents gadgets...

4- It is on the drawing board but I am thinking of adding the posibility of choosing langunage, not only of the menu but also of the series data
 (the api of the series allow to get the data in different languages, but it is neccesary to change the database model to
 allow to save at the same time information of the series and episdoes in differente language in order to allow users to change
 the language whenever the want and avoid to reload all the info)