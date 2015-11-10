# ScientificCalculator
OOP using Java Semester Project

Things that need to be done:   
1. We need someone to create a fragment (ScientificFunctions) for the scientific calculator aspect  
2. This fragment should define all buttons and have a unique string that they pass through the interface when pressed  
3. I got the SimpleCalculator fragment to work but it should be tweaked so it fits better  
4. We need a way to call the SimpleFragment and ScientificFunctions when the app gets rotated. This will will include finding a way to rescale the two fragments. We may need to create a new activity to do so.  
5. We need to make the buttons and UI look nice. This involes altering the XML files. Text and Colors should  
Match between SimpleCalculator and ScientificFunctions  
  
Completed:  
1. SimpleCalculator xml layout works fine, I'm working on handling the text formatting right now.  
2. HandleDisplay is where all the text alterations are done right now.  
3. I'm working on UserInput which will be used to solve the equation. This will have to be combined with how ever we   decide to handle the ScientificFunctions class

  
For Questions on using fragments, I uploaded a brief example on them on my github account  

This is what it looks like. We need to give the TextView a visible box and a cursor to show where the user is.  
The keyboard should also be shifted downward  
  <img width="330" alt="screen shot 2015-11-08 at 2 06 54 pm" src="https://cloud.githubusercontent.com/assets/11150496/11022032/2f5b110c-8622-11e5-9442-5816607427a5.png">
