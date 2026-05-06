# UNO

// UNIONE BRANCH
git config pull.rebase false
git pull origin main
git push origin main

// DIVISIONE BRANCH
git checkout -b nome-del-ramo-che-voglio

// BRANCH
git switch nome-del-ramo-che-voglio / main
git add .                                   // AGGIUNGO IL FILE AL COMMIT
git commit -m "messaggio da pushare"
git push -u origin push-dell-ramo
git merge nome-del-ramo-che-voglio
git branch -D nome-del-ramo                 // ELIMINO RAMO
git push origin --delete ramo-che-voglio    // ELIMINO RAMO PUSHATO