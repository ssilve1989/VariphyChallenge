## Variphy Brewery API Challenge

Write a Java program or application, using any type of Java libraries or technologies that you wish, that accepts a minimum and/or maximum ABV as well as an optional search keyword and returns/displays the following information for beers made by the Harpoon Brewery (ID = RzvedX) matching the specified search criteria.

- Beer Name
- ID
- Description
- ABV
- IBU
- Created Date formatted as Month/Day/Year

The search keyword should be used to search by the beer name or description values.  The search results should be displayed in descending order (newest first) by Create Date.

If the API doesn't return a necessary value (such as ABV or description) for a beer, then a suitable value from the element/property "style" should be used instead.


Example Output for Search Keyword: "Black IPA" and ABV < 8.0:

```
Name: Harpoon Black IPA
Id: fJ3BhE
Description: American-style Black Ales are very dark to black and perceived to have medium high to high hop bitterness, flavor and aroma with medium-high alcohol content, balanced with a medium body. Fruity, floral and herbal character from hops of all origins may contribute character. The style is further characterized by a balanced and moderate degree of caramel malt and dark roasted malt flavor and aroma. High astringency and high degree of burnt roast malt character should be absent.
ABV: 7.5
IBU: 41
Create Date: 10/24/2012

Name: 100 Barrel Series #40 Black IPA
Id: lOgUYP
Description: We have enjoyed brewing and drinking beers brewed in the IPA style for a long time. Our flagship Harpoon IPA and our Leviathan Imperial IPA are two of our favorites. We thought it was about time to take a crack at the Black IPA style.
ABV: 7
IBU: 67
Create Date: 3/21/2012
```

## Assumptions
I assume that if an item has a minimum ABV value of 5.0 and a maximum ABV value of 10.0 that invoking this program with an argument of minimum ABV = 6.1 will return this result as it can *sometimes* meet the minimum ABV of 5.

## Notes
Java is fairly poor when dealing with JSON causing things to be overly verbose. I chose to do this as a CLI application to showcase more raw Java capabilities and style. In reality, in a web application context, I would likely let JavaScript deal with the JSON and only use Java to make a request to the service, if even that. Generally it is better to let JavaScript deal with it, especially if you would be going back/forth to the server with different filters/requests on the initial json object.

Some other design notes can be found through comments in each class as to why I did certain things and what I would do differently in a production environment.