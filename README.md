# Kitchen secret - Recipe app

## Functionalities 

1. Add new recipe: Type ingredients and method. Add tag for searching later (future improvement). Generate image for illustration.
2. View list of recipes: in home screen, a list of recipes are displayed with its name, ingredients and image. User can see if they have enough ingredients to make the recipe so they can click on the recipe name for more details.
3. View details: Tap the recipe name in home screen. Select the pencil icon to edit while viewing.
4. Edit recipe: Select the pencil icon next to recipe name in homescreen.
5. Delete recipe: Select the trash button next to recipe name in homescreen.

<img width="900" alt="Screenshot 2024-07-01 at 3 20 07 PM" src="https://github.com/ThuHaNgo11/NoteApp/assets/104910443/f49b16f3-f424-4584-bab6-e5d80f5f51fb">

## Application architecture

![mvvm](https://github.com/ThuHaNgo11/NoteApp/assets/104910443/b7a871bd-a9d1-489a-a6de-6e9395fa5220)

## Implementation note

1. Go to https://platform.openai.com/api-keys to generate your API key for image generation.
2. In the local.properties (in Gradle Scripts folder), add imageGenerationApiKey="your_API_key".
3. Sync gradle, rebuild app, start creating your collection of kitchen secret 🍮 🌮 🥗

## Demo link
https://youtu.be/WJI1784yrLc?si=9GPNzI0YSAYbSAI-

