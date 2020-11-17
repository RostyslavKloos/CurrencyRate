# CurrencyRate


A CurrencyRate app that loads information from [NBU API] (https://bank.gov.ua/ua/open-data/api-dev), [PrivatBank Api] (https://api.privatbank.ua/), [CBR bank API] (http://www.cbr.ru/development/sxml/),

to show one approach to using some of the best practices in Android Development.

Including:  
 * ViewModel
 * LiveData
 * Kotlin Coroutines
 * Retrofit
 * Hilt
 * Navigation
 * GraphView
 * Swiperefreshlayout

<p>
<img src="https://github.com/RostyslavKloos/CurrencyRate/blob/master/assets/rate_currency_main2.png" width="260">
<img src="https://github.com/RostyslavKloos/CurrencyRate/blob/master/assets/rate_currency_land.png" width="400">

<img src="https://github.com/RostyslavKloos/CurrencyRate/blob/master/assets/rate_graph_result.png" width="260">
<img src="https://github.com/RostyslavKloos/CurrencyRate/blob/master/assets/rate_graph_land.png" width="400">
 </p>

#### The app has following packages:
1. **data**: It contains all the data accessing and manipulating components.
2. **di**: Dependency providing classes using Dagger2 (Hilt).
3. **ui**: View classes along with their corresponding ViewModel.
4. **utils**: Utility classes.
