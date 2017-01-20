# -*- coding: utf-8 -*-
"""
Created on Wed Jan 18 18:26:33 2017

@author: Aravindharaj

Description:
This is a simple Web Scraping program using the lxml and BeautifulSoup packages. 
It first gets all the products listed in the Men's Shirting section of the online shopping website: www.overstock.com
Then, the program loads and parses each individual product website iteratively and scrapes different contents from the product website.
Finally, the scraped contents are loaded into the MongoDB database.
"""
import urllib.request
import lxml.html
from bs4 import BeautifulSoup
import re
from pymongo import MongoClient

# Function to scrape the contents of a product webpage from the overstock.com website

def getProducts(product_link):
    child_page = urllib.request.urlopen(product_link)
    child_page_result = lxml.html.fromstring(child_page.read())
    
    # Getting the Product title
    product = child_page_result.xpath('//div[@class="product-title"]//h1/text()')
    product_title = product[0]
    
    # Getting the overall Rating of the Product
    rating = child_page_result.xpath('//div[@class="row ratings-container"]//div[@class="ratings"]//span[@class="stars"]')
    product_rating = rating[0].get('data-rating')
    
    # Getting the total number of Reviews given for the Product
    reviews = child_page_result.xpath('//div[@class="row ratings-container"]//div[@class="ratings"]//span[@class="count"]')
    product_reviews = reviews[0].text
    if (product_reviews == "Review this item"):
        product_reviews = "None"
    else:
        product_reviews = re.findall(r'\d+',product_reviews)[0]
    
    # Getting the Starting Price of the Product
    price = child_page_result.xpath('//form[@id="addToCartForm"]//div[@class="row pricing-section"]//span[@class="monetary-price-value"]')
    product_price = price[0].text
    
    # Getting the ID which is unique for each and every Product (Note: Variants of the same Product have the same ID)
    id = child_page_result.xpath('//form[@id="addToCartForm"]//div[@class="item-number"]')
    product_id = id[0].text
    product_id = re.findall(r'\d+',product_id)[0]

    # Getting the Specifications of the Product
    rows = []
    specs = child_page_result.xpath('//section[@class="content-section" and @id="more"]//div[@class="toggle-content"]//table[@class="table table-dotted table-extended table-header translation-table"]')
    
    if len(specs) > 0:
        table = lxml.html.tostring(specs[0])
        soup = BeautifulSoup(table, "html.parser")
        count = 0
        for tr in soup.find_all('tr'):
            row = []
            for td in tr.find_all('td'):
                if (count == 0):
                    key = td.string
                    key = re.sub(' +','',key)
                    table_key = key.replace('\n','')
                    count = count+1
                    row.append(table_key)
                else:
                    value = td.string
                    value = re.sub(' +','',value)
                    table_value = value.replace('\n','')
                    count = 0
                    row.append(table_value)
            if row:
                rows.append(row)
    
    # Getting the overview of the Reviews given for the Product
    reviews_summary = []
    reviews_label = child_page_result.xpath('//section[@id="reviews"]//div[@class="reviews-wrapper-row row"]//div[@class="row"]//div[@class="row"]//div[@class="col-xs-2"]//div[@class="label"]')
    reviews_value = child_page_result.xpath('//section[@id="reviews"]//div[@class="reviews-wrapper-row row"]//div[@class="row"]//div[@class="row"]//div[@class="col-xs-1"]//div[@class="append"]')
    for label, value in zip(reviews_label,reviews_value):
        review_summary = []
        r_label = label.text
        r_label = re.sub(' ','_',r_label)
        r_label = re.sub('\s+','',r_label)
        r_value = value.text
        r_value = re.sub('\s+','',r_value)
        review_summary.append(r_label)
        review_summary.append(r_value)
        reviews_summary.append(review_summary)
    
    # Getting all the detailed Reviews for the Product
    reviews = child_page_result.xpath('//section[@id="reviews"]//div[@class="row review-posts"]//ul[@class="reviews recent recentOnly"]//li[@class="review"]')
    rev = []
    for review in reviews:
        r = {}
        review_soup = BeautifulSoup(lxml.html.tostring(review),"html.parser")
        for header in review_soup.find_all('header',{"itemprop":"name"}):
            r['header'] = header.string
        for div in review_soup.find_all('div',{"class":"bd"}):
            for span in div.find_all('span',{"class":"stars"}):
                r['rating'] = span.get('data-rating')
            vp_span = div.find_all('span',{"class":"verified-purchase"})
            if len(vp_span) > 0:
                r['is_verified_purchase'] = "Yes"
            else:
                r['is_verified_purchase'] = "No"
            for p in div.find_all('p',{"itemprop":"reviewBody"}):
                r['review'] = p.string
        for footer in review_soup.find_all('footer'):
            for user_span in footer.find_all('span',{"class":"user"}):
                r['user'] = user_span.string
            for date_span in footer.find_all('span',{"class":"date"}):
                r['posted_date'] = date_span.string
        rev.append(r)
    
    # Preview of the scraped data that will be stored in the MongoDB database
    data = {"id":product_id, "title":product_title, "ratings":product_rating, "reviews" : product_reviews, "starting_price": product_price, "specifications": rows, "reviews_summary": reviews_summary, "most_recent_reviews": rev}
    print("\n")
    print(data)

    # Connecting and inserting the data into the MongoDB database
    client = MongoClient('localhost', 27017)
    db = client['scraping_db']
    collection = db['result']
    insert_id = collection.insert_one(data).inserted_id
    if insert_id:
        print("\n======Successfully inserted the product " + product_id + " into the MongoDB database======")
    else:
        print("\n======There was a problem in inserting the product "+product_id+"======")

# Static URL from which the list of products are scraped and feeded into the getProducts function for further scraping
url = "https://www.overstock.com/Clothing-Shoes/Shirts/3410/cat.html"
parent_page = urllib.request.urlopen(url)
parent_page_result = lxml.html.fromstring(parent_page.read())
for link in parent_page_result.xpath('//div[@class="product-info"]//a/@href'):
    getProducts(link)