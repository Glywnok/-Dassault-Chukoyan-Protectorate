from pathlib import Path

#USELESS

filepath = Path(__file__).parent / "dcp_warlock.ship" #insert file here :)

# creating a variable and storing the text
# that we want to search
search_text = "istl"
  
# creating a variable and storing the text
# that we want to add
replace_text = "dcp"

# creating a variable and storing the text
# that we want to search
search_text1 = "istlx"
  
# creating a variable and storing the text
# that we want to add
replace_text1 = "dcpx"

# creating a variable and storing the text
# that we want to search
search_text2 = "ISTL"
  
# creating a variable and storing the text
# that we want to add
replace_text2 = "DCP"


# creating a variable and storing the text
# that we want to search
search_text3 = "dcp"
  
# creating a variable and storing the text
# that we want to add
replace_text3 = "dcp"

# creating a variable and storing the text
# that we want to search
search_text4 = "dcpx"
  
# creating a variable and storing the text
# that we want to add
replace_text4 = "dcpx"

# creating a variable and storing the text
# that we want to search
search_text5 = "DMP"
  
# creating a variable and storing the text
# that we want to add
replace_text5 = "DCP"
  
# Opening our text file in read only
# mode using the open() function
with open(filepath, "r") as file:
  
    # Reading the content of the file
    # using the read() function and storing
    # them in a new variable
    data = file.read()
  
    # Searching and replacing the text
    # using the replace() function
    data = data.replace(search_text4, replace_text4)

    # Searching and replacing the text
    # using the replace() function
    data = data.replace(search_text3, replace_text3)
    
    
    # Searching and replacing the text
    # using the replace() function
    data = data.replace(search_text5, replace_text5)
  
  
    # Searching and replacing the text
    # using the replace() function
    data = data.replace(search_text1, replace_text1)
  
    # Searching and replacing the text
    # using the replace() function
    data = data.replace(search_text, replace_text)

    # Searching and replacing the text
    # using the replace() function
    data = data.replace(search_text2, replace_text2)
  
# Opening our text file in write only
# mode to write the replaced content
with open(filepath, "w") as file:
  
    # Writing the replaced data in our
    # text file
    file.write(data)
  
# Printing Text replaced
print("Text replaced")