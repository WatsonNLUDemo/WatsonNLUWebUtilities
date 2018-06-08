dest <- "~/UNGA_Speeches/70/OCRed"
myfiles <- list.files(path = dest, pattern = "pdf",  full.names = TRUE)


############### PDF (text format) to TXT ###################

##### Wait! #####
# Before proceeding, make sure you have a copy of pdf2text
# on your computer! Details: https://en.wikipedia.org/wiki/Pdftotext
# Download: http://www.foolabs.com/xpdf/download.html
# If you have a PDF with text, ie you can open the PDF in a 
# PDF viewer and select text with your curser, then use these 
# lines to convert each PDF file that is named in the vector 
# into text file is created in the same directory as the PDFs
# note that my pdftotext.exe is in a different location to yours


lapply(myfiles, function(i) system(paste('"C:/Program Files/xpdfbin-win-3.04/bin64/pdftotext.exe"', paste0('"', i, '"')), wait = FALSE) )

# where are the txt files you just made?
dest # in this folder

# And now you're ready to do some text mining on the text files

