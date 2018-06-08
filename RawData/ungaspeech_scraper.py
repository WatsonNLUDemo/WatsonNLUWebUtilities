# -*- coding: utf-8 -*-
"""
Created on Mon Aug  7 14:15:35 2017

@author: EUR
"""


###############################################################################
#
#SCRAPE PDFs FROM UN WEBSITE
#
###############################################################################

##ENTER SESSION NUMBER HERE##
session = 71
yr = str(1945+session)

##This section collects the links to the country pages, which each hold the speech
from bs4 import BeautifulSoup
from urllib.request import Request, urlopen
 
#Enter the URL to the UN page that has the links to the country pages for speeches
#There should be one for each session, or one for each day of each session
req = Request("https://gadebate.un.org/en/listbydate/2016-09-22")
html_page = urlopen(req)
soup = BeautifulSoup(html_page, "lxml")
 
#Get all of the links on the page
links = []
for link in soup.findAll('a'):
    links.append(link.get('href'))

#subset to only the country page links ####THIS MAY NEED TO CHANGE###
links = links[23:61]
 
#print(links)


#imports the csv with the country name to iso3 code converter
import csv
iso_cc = [] #create a blank list which will hold the conversions

#open the csv and write each row to an index of the iso_cc list. This creates
    #a standardized list with the first three letters as the iso3 code and 
    #the end of each string in the list with the official UN country name
with open('./Documents/UNGA_Speeches/iso_to_country.csv', newline='') as csvfile:
     iso_to_country = csv.reader(csvfile, delimiter=' ', quotechar='|')
     for row in iso_to_country:
         iso_cc.append(' '.join(row))
         #print(' '.join(row))


   
import requests
#opens a blank text file to write the information about each speech to
with open('./Documents/UNGA_Speeches/%s/unga_%s_meta' % (session, session), 'w', newline='') as f1:
    unga_meta = csv.writer(f1, delimiter='|')
    #loop through each link to each country page
    for i in range(0, len(links)):
        page = requests.get("https://gadebate.un.org" + links[i])
        soup2 = BeautifulSoup(page.content, 'html.parser')
        #print(soup2.prettify())
        
        #first get the data about speaker, country, and date of the speech
        meta = str(soup2.find_all(id="statement-speaker-and-title")).split(">")
        country = meta[2][0:-4]
        speaker = meta[4][0:-4]
        #remove special letters from speaker names which can't be put into the csv
        if "š" in speaker:
            speaker = speaker.replace("š", "s")
        if "ć" in speaker:
            speaker = speaker.replace("ć", "c")
        if "ğ" in speaker:
            speaker = speaker.replace("ğ", "g")
        if "ē" in speaker:
            speaker = speaker.replace("ē", "e")
        if "Č" in speaker:
            speaker = speaker.replace("Č", "C")
        if "á" in speaker:
            speaker = speaker.replace("á", "a")
        if "č" in speaker:
            speaker = speaker.replace("č","c")
        if "ș" in speaker:
            speaker = speaker.replace("ș", "s")
        if "ạ" in speaker:
            speaker = speaker.replace("ạ", "a")
        
        #get the date - need to do this because there are blank spaces in the date
        f = len(meta[6]) - len(meta[6].lstrip())
        date = meta[6][f:-meta[6].index(yr)-1]
        
        #create a boolean to see if the country code is in the converter list
            #this will remain false for countries whose names are not the same as 
            #in the converter list or for people/organizations that are not countries
        changed = False
        
        #loop through the converter
        for c in iso_cc:
            if country == c[4:]: #if the country name is in the converter
                cou = str(c[0:3]) #save the country code for that name
                changed = True 
        if changed == False: #if there was no match
            cou = country[0:3] #use the first three letters as the country code
            
        #create a blank list for all of the pdf files. This will be between 0 if 
            #a country doesn't have a posted statement to however many statements
            #there are based on the number of languages available 
        pdf_link = ""
        pdf_links = []
        for link in soup2.findAll('a'): #find all of the pdfs and add them to the list
            if ".pdf" in str(link):
                pdf_links.append(str(link))
        
        english = False #set to True only if an English version is available
        
        #pull the one in English (if it exists) because this is what we download
        for j in pdf_links: 
            if "English" in j:
                pdf_link = str(j)
                english = True
                
        #if no English version is available and there's only one PDF available then pull 
            #that PDF
        if english == False:
            if len(pdf_links) == 1 and "English" not in pdf_links[0]:
                pdf_link = str(pdf_links[0])
             #if there are more than one language available then pull in the hierarchy:
                 #1. French, 2. Spanish, 3. Russian, 4. Arabic
            else:  
                for j in pdf_links:
                   if "Arabic" in j:
                       pdf_link = str(j)
                for j in pdf_links:
                   if "Russian" in j:
                       pdf_link = str(j)                
                for j in pdf_links:
                   if "Spanish" in j:
                       pdf_link = str(j)                
                for j in pdf_links:
                   if "French" in j:
                       pdf_link = str(j)
                
        language = "" #empty string to record the languages available
        #concatenate into one string the different languages available
        for k in pdf_links:
            l = str(k).split("\"")[2][1:-4]
            language = language + l + " "
        
        #save session #, country name, country code, speaker, date, and language(s)
            #into the csv we opened at the top of this section
        unga_meta.writerow([session, country, cou, speaker, date, language])
        print(i) #counter to see progress

        #soup2.findAll('a')[22]
        
        #if there is no statement, then iterate the loop to the next value
        if pdf_link == "":
            continue
        
        #subset to the lnik itself
        pdf_link = str(pdf_link).split("\"")[1]
    
        #get the pdf using the link
        response = requests.get(pdf_link)
        if(english == True):
        #save the pdf using the session number and country code as the file name
            with open(('./Documents/UNGA_Speeches/%s/PDFs_%s/%s_%s.pdf'%(session,session,cou,session)), 'wb') as f:
                f.write(response.content)
        else:
            with open(('./Documents/UNGA_Speeches/%s/Need_Translation/%s_%s.pdf'%(session,cou,session)), 'wb') as f:
                f.write(response.content)
            























